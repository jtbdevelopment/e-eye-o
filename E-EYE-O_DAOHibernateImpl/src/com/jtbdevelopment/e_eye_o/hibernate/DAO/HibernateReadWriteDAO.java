package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.IdObjectUpdateHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.events.EventFactory;
import com.jtbdevelopment.e_eye_o.entities.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Date: 11/19/12
 * Time: 5:34 PM
 */
@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
@SuppressWarnings("unused")
public class HibernateReadWriteDAO extends HibernateReadOnlyDAO implements ReadWriteDAO {
    private final IdObjectUpdateHelper idObjectUpdateHelper;
    private final EventBus eventBus;
    private final EventFactory eventFactory;

    @Autowired
    public HibernateReadWriteDAO(final EventBus eventBus, final EventFactory eventFactory, final SessionFactory sessionFactory, final DAOIdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper, final IdObjectUpdateHelper idObjectUpdateHelper) {
        super(sessionFactory, wrapperFactory, idObjectReflectionHelper);
        this.idObjectUpdateHelper = idObjectUpdateHelper;
        this.eventBus = eventBus;
        this.eventFactory = eventFactory;
    }

    @Override
    public <T extends IdObject> T create(final T entity) {
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You cannot explicitly create a DeletedObject.");
        }
        final T wrapped = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().save(wrapped);
        dealWithNewObservations(wrapped);
        if (wrapped instanceof AppUserOwnedObject) {
            saveHistory((AppUserOwnedObject) wrapped);
            if (eventBus != null) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.ADDED, (AppUserOwnedObject) wrapped));
            }
        } else {
            if (eventBus != null) {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.ADDED, wrapped));
            }
        }
        return wrapped;
    }

    private void saveHistory(final AppUserOwnedObject appUserOwnedObject) {
        if (appUserOwnedObject instanceof AppUserSettings || appUserOwnedObject instanceof TwoPhaseActivity) {
            return;
        }
        if (idObjectSerializer == null) {
            idObjectSerializer = applicationContext.getBean(IdObjectSerializer.class);
        }
        HibernateHistory hibernateHistory = new HibernateHistory();
        hibernateHistory.setAppUser(appUserOwnedObject.getAppUser());
        hibernateHistory.setModificationTimestamp(appUserOwnedObject.getModificationTimestamp());
        hibernateHistory.setSerializedVersion(idObjectSerializer.write(appUserOwnedObject));
        sessionFactory.getCurrentSession().save(hibernateHistory);
    }

    @Override
    public <T extends IdObject> T update(final AppUser updatingUser, final T entity) {
        if (!idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass()).getAnnotation(IdObjectEntitySettings.class).editable()) {
            throw new IllegalArgumentException("You cannot explicitly update a " + entity.getClass() + ".");
        }
        final T existing = get((Class<T>) entity.getClass(), entity.getId());
        idObjectUpdateHelper.validateUpdates(updatingUser, existing, entity);
        sessionFactory.getCurrentSession().clear();
        return internalUpdate(entity);
    }

    private <T extends IdObject> T internalUpdate(final T entity) {
        final T wrapped = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().update(wrapped);
        dealWithObservationUpdatesOrDeletes(wrapped);
        if (entity instanceof AppUserOwnedObject) {
            sessionFactory.getCurrentSession().flush();
            saveHistory((AppUserOwnedObject) wrapped);
            if (eventBus != null) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.MODIFIED, (AppUserOwnedObject) wrapped));
            }
        } else {
            if (eventBus != null) {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.MODIFIED, wrapped));
            }
        }
        return wrapped;
    }

    @Override
    public <T extends AppUserOwnedObject> void changeArchiveStatus(final T entity) {
        boolean initialArchivedState = entity.isArchived();
        boolean newArchivedState = !initialArchivedState;

        Session currentSession = sessionFactory.getCurrentSession();

        T wrapped = wrapperFactory.wrap(entity);
        wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        if (wrapped == null) {
            return;  //  Already deleted?
        }
        if (entity instanceof ClassList) {
            for (Student student : getAllStudentsForClassList((ClassList) entity)) {
                if (student.isArchived() == initialArchivedState) {
                    if (!newArchivedState || student.getActiveClassLists().size() == 1) {
                        changeArchiveStatus(student);
                    }
                }
            }
        }

        for (Photo photo : getAllPhotosForEntity(entity)) {
            if (photo.isArchived() == initialArchivedState) {
                changeArchiveStatus(photo);
            }
        }

        if (entity instanceof Observable) {
            for (Observation observation : getAllObservationsForEntity((Observable) entity)) {
                if (observation.isArchived() == initialArchivedState) {
                    changeArchiveStatus(observation);
                }
            }
        }

        wrapped.setArchived(newArchivedState);
        currentSession.update(wrapped);
        saveHistory(wrapped);
        if (eventBus != null) {
            eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.MODIFIED, wrapped));
        }
    }

    @Override
    public TwoPhaseActivity activateUser(final TwoPhaseActivity relatedActivity) {
        Session currentSession = sessionFactory.getCurrentSession();
        relatedActivity.setArchived(true);
        AppUser wrappedAppUser = wrapperFactory.wrap(relatedActivity.getAppUser());
        wrappedAppUser.setActive(true);
        wrappedAppUser.setActivated(true);
        currentSession.update(wrappedAppUser);
        TwoPhaseActivity wrappedRelatedActivity = wrapperFactory.wrap(relatedActivity);
        currentSession.update(wrappedRelatedActivity);
        return wrappedRelatedActivity;
    }

    @Override
    public TwoPhaseActivity resetUserPassword(final TwoPhaseActivity relatedActivity, final String newPassword) {
        Session currentSession = sessionFactory.getCurrentSession();
        relatedActivity.setArchived(true);
        AppUser wrappedAppUser = wrapperFactory.wrap(relatedActivity.getAppUser());
        wrappedAppUser.setActive(true);
        wrappedAppUser.setActivated(true);
        wrappedAppUser.setPassword(newPassword);
        currentSession.update(wrappedAppUser);
        TwoPhaseActivity wrappedRelatedActivity = wrapperFactory.wrap(relatedActivity);
        currentSession.update(wrappedRelatedActivity);
        return wrappedRelatedActivity;
    }

    @Override
    public AppUser updateAppUserLogout(final AppUser appUser) {
        AppUser loaded = get(AppUser.class, appUser.getId());
        loaded.setLastLogout(new DateTime());
        return internalUpdate(loaded);
    }

    @Override
    public AppUserSettings updateSettings(final AppUser appUser, final Map<String, Object> settings) {
        AppUserSettings userSettings = getEntitiesForUser(AppUserSettings.class, appUser).iterator().next();
        userSettings.updateSettings(settings);
        sessionFactory.getCurrentSession().update(userSettings);
        saveHistory(userSettings);
        return userSettings;
    }

    //  TODO - mark delete and allow undelete
    @Override
    @SuppressWarnings("unchecked")
    public void deleteUser(final AppUser appUser) {
        Session currentSession = sessionFactory.getCurrentSession();

        AppUser wrapped = wrapperFactory.wrap(appUser);
        wrapped = get(AppUser.class, wrapped.getId());
        if (wrapped == null) {
            return;  //  Already deleted?
        }

        Collection<AppUserOwnedObject> filtered = Collections2.filter(getEntitiesForUser(AppUserOwnedObject.class, wrapped), new Predicate<AppUserOwnedObject>() {
            @Override
            public boolean apply(@Nullable AppUserOwnedObject input) {
                return input != null && !(input instanceof DeletedObject);
            }
        });
        for (AppUserOwnedObject entity : filtered) {
            delete(entity);
        }
        for (DeletedObject deletedObject : getEntitiesForUser(DeletedObject.class, wrapped)) {
            currentSession.delete(deletedObject);
        }

        currentSession.delete(wrapped);
        if (eventBus != null) {
            eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.DELETED, wrapped));
        }
        //  TODO - test
    }

    //  TODO - mark delete and allow undelete
    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> void delete(final T entity) {
        Set<AppUserOwnedObject> updatedItems = new HashSet<>();
        Set<AppUserOwnedObject> deletedItems = new HashSet<>();
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You can not manually delete DeletedObjects.  These are only cleaned up by deleting user.");
        }
        Session currentSession = sessionFactory.getCurrentSession();

        T wrapped = wrapperFactory.wrap(entity);
        wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        if (wrapped == null) {
            //  Already deleted
            return;
        }

        if (wrapped instanceof ObservationCategory) {
            for (Observation observation : getAllObservationsForObservationCategory((ObservationCategory) wrapped)) {
                observation.removeCategory((ObservationCategory) wrapped);
                currentSession.update(observation);
                saveHistory(observation);
                updatedItems.add(observation);
            }
        }
        if (wrapped instanceof ClassList) {
            for (Student student : getAllStudentsForClassList((ClassList) wrapped)) {
                student.removeClassList((ClassList) wrapped);
                currentSession.update(student);
                saveHistory(student);
                updatedItems.add(student);
            }
        }
        final List<Photo> allPhotosForEntity = getAllPhotosForEntity(wrapped);
        for (Photo p : allPhotosForEntity) {
            deletedItems.add(p);
            currentSession.delete(p);
        }
        if (wrapped instanceof Observable) {
            for (Observation o : getAllObservationsForEntity((Observable) wrapped)) {
                final List<Photo> allPhotosForObservation = getAllPhotosForEntity(o);
                for (Photo p : allPhotosForObservation) {
                    deletedItems.add(p);
                    currentSession.delete(p);
                }
                deletedItems.add(o);
                currentSession.delete(o);
            }
        }

        deletedItems.add(wrapped);
        currentSession.delete(wrapped);
        Observable observable = dealWithObservationUpdatesOrDeletes(wrapped);
        if (observable != null) {
            updatedItems.add(observable);  //  Flush forced internally
        }
        currentSession.flush();   //  Force any delete object generations inside this session
        //  TODO - unit test adjustments for updated/deletedItems verification
        publishChanges(updatedItems, deletedItems);
    }

    private void publishChanges(Set<AppUserOwnedObject> updatedItems, Set<AppUserOwnedObject> deletedItems) {
        if (eventBus != null) {
            for (AppUserOwnedObject updaedItem : updatedItems) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.MODIFIED, updaedItem));
            }
            for (AppUserOwnedObject deletedItem : deletedItems) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, deletedItem));
            }
        }
    }

    private void dealWithNewObservations(final IdObject idObject) {
        if (idObject instanceof Observation) {
            Observation observation = (Observation) idObject;
            Observable observable = observation.getObservationSubject();
            if (observable == null) {
                throw new RuntimeException("observable is null");
            }
            if (observation.getObservationTimestamp() == null) {
                throw new RuntimeException("observationTimeStamp is null");
            }
            if (observable.getLastObservationTimestamp() == null) {
                throw new RuntimeException("lastObservationTimestamp is null");
            }
            if (observation.getObservationTimestamp().compareTo(observable.getLastObservationTimestamp()) > 0) {
                observable.setLastObservationTimestamp(observation.getObservationTimestamp());
                internalUpdate(observable);
            }
        }
    }

    //  TODO - not efficient, but easy to code
    private Observable dealWithObservationUpdatesOrDeletes(final IdObject idObject) {
        if (idObject instanceof Observation) {
            Observation observation = (Observation) idObject;
            Observable observable = observation.getObservationSubject();
            LocalDateTime lastObservationTimestamp = getLastObservationTimestampForEntity(observable);
            if (lastObservationTimestamp.compareTo(observable.getLastObservationTimestamp()) != 0) {
                observable.setLastObservationTimestamp(lastObservationTimestamp);
                internalUpdate(observable);
                return observable;
            }
        }
        return null;
    }
}
