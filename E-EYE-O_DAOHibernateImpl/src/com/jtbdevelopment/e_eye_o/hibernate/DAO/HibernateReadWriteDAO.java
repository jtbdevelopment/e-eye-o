package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.IdObjectUpdateHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.events.EventFactory;
import com.jtbdevelopment.e_eye_o.entities.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public HibernateReadWriteDAO(final EventBus eventBus, final EventFactory eventFactory, final SessionFactory sessionFactory, final IdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper, final IdObjectUpdateHelper idObjectUpdateHelper) {
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
        final T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        sessionFactory.getCurrentSession().save(wrapped);
        dealWithNewObservations(wrapped);
        if (wrapped instanceof AppUserOwnedObject) {
            saveHistory((AppUserOwnedObject) wrapped);
        }

        publishCreate(wrapped);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        return (T) get(wrapped.getClass(), wrapped.getId());
    }

    private void saveHistory(final AppUserOwnedObject appUserOwnedObject) {
        if (appUserOwnedObject instanceof AppUserSettings || appUserOwnedObject instanceof TwoPhaseActivity) {
            return;
        }
        IdObjectSerializer idObjectSerializer = getJSONIdObjectSerializer();
        sessionFactory.getCurrentSession().flush();  // Force update so timestamp is updated
        sessionFactory.getCurrentSession().clear();  // Force update so timestamp is updated
        AppUserOwnedObject reloaded = get(AppUserOwnedObject.class, appUserOwnedObject.getId());
        HibernateHistory hibernateHistory = new HibernateHistory();
        hibernateHistory.setAppUser(reloaded.getAppUser());
        hibernateHistory.setModificationTimestamp(reloaded.getModificationTimestamp());
        hibernateHistory.setSerializedVersion(idObjectSerializer.write(reloaded));
        sessionFactory.getCurrentSession().save(hibernateHistory);
    }

    @Override
    public <T extends IdObject> T update(final AppUser updatingUser, final T entity) {
        if (!idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass()).getAnnotation(IdObjectEntitySettings.class).editable()) {
            throw new IllegalArgumentException("You cannot explicitly update a " + entity.getClass() + ".");
        }
        final T existing = get((Class<T>) entity.getClass(), entity.getId());
        idObjectUpdateHelper.vetInvalidFieldUpdates(updatingUser, existing, entity);
        sessionFactory.getCurrentSession().clear();
        return internalUpdate(entity);
    }

    private <T extends IdObject> T internalUpdate(final T entity) {
        final T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        sessionFactory.getCurrentSession().update(wrapped);
        dealWithObservationUpdatesOrDeletes(wrapped);
        if (entity instanceof AppUserOwnedObject) {
            sessionFactory.getCurrentSession().flush();
            saveHistory((AppUserOwnedObject) wrapped);
        }
        publishUpdate(wrapped);
        sessionFactory.getCurrentSession().flush();
        sessionFactory.getCurrentSession().clear();
        return (T) get(wrapped.getClass(), wrapped.getId());
    }

    @Override
    public <T extends AppUserOwnedObject> T changeArchiveStatus(final T entity) {
        boolean initialArchivedState = entity.isArchived();
        boolean newArchivedState = !initialArchivedState;

        Session currentSession = sessionFactory.getCurrentSession();

        T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        if (wrapped == null) {
            return null;  //  Already deleted?
        }

        if (!newArchivedState) {
            //  If un-archiving, un-archive this one first
            wrapped.setArchived(newArchivedState);
            wrapped = internalUpdate(wrapped);
            currentSession.flush();
            currentSession.clear();
            wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        }

        for (Photo photo : getAllPhotosForEntity(wrapped, 0, 0)) {
            if (photo.isArchived() == initialArchivedState) {
                changeArchiveStatus(photo);
            }
        }

        if (wrapped instanceof Semester) {
            for (Observation observation : getAllObservationsForSemester((Semester) wrapped, 0, 0)) {
                if (observation.isArchived() == initialArchivedState) {
                    changeArchiveStatus(observation);
                }
            }
        }

        if (wrapped instanceof Observable) {
            for (Observation observation : getAllObservationsForEntity((Observable) wrapped)) {
                if (observation.isArchived() == initialArchivedState) {
                    changeArchiveStatus(observation);
                }
            }
        }

        if (wrapped instanceof ClassList) {
            for (Student student : getAllStudentsForClassList((ClassList) wrapped)) {
                if (student.isArchived() == initialArchivedState) {
                    if (!newArchivedState || student.getActiveClassLists().size() == 1) {
                        changeArchiveStatus(student);
                    }
                }
            }
        }

        currentSession.flush();
        currentSession.clear();

        if (newArchivedState) {
            wrapped = (T) get(wrapped.getClass(), wrapped.getId());
            //  If un-archiving, un-archive this one last
            wrapped.setArchived(newArchivedState);
            wrapped = internalUpdate(wrapped);
        }
        return wrapped;
    }

    @Override
    public TwoPhaseActivity activateUser(final TwoPhaseActivity relatedActivity) {
        Session currentSession = sessionFactory.getCurrentSession();
        relatedActivity.setArchived(true);
        AppUser wrappedAppUser = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, relatedActivity.getAppUser());
        wrappedAppUser.setActive(true);
        wrappedAppUser.setActivated(true);
        currentSession.update(wrappedAppUser);
        TwoPhaseActivity wrappedRelatedActivity = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, relatedActivity);
        currentSession.update(wrappedRelatedActivity);
        return wrappedRelatedActivity;
    }

    @Override
    public TwoPhaseActivity updateUserEmailAddress(final TwoPhaseActivity changeRequest, final String newAddress) {
        Session currentSession = sessionFactory.getCurrentSession();
        changeRequest.setArchived(true);
        AppUser wrappedAppUser = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, changeRequest.getAppUser());
        wrappedAppUser.setEmailAddress(newAddress);
        currentSession.update(wrappedAppUser);
        TwoPhaseActivity wrappedRelatedActivity = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, changeRequest);
        currentSession.saveOrUpdate(wrappedRelatedActivity);
        publishUpdate(wrappedAppUser);
        return wrappedRelatedActivity;
    }

    @Override
    public TwoPhaseActivity resetUserPassword(final TwoPhaseActivity relatedActivity, final String newPassword) {
        Session currentSession = sessionFactory.getCurrentSession();
        relatedActivity.setArchived(true);
        AppUser wrappedAppUser = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, relatedActivity.getAppUser());
        wrappedAppUser.setActive(true);
        wrappedAppUser.setActivated(true);
        wrappedAppUser.setPassword(newPassword);
        currentSession.update(wrappedAppUser);
        TwoPhaseActivity wrappedRelatedActivity = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, relatedActivity);
        currentSession.update(wrappedRelatedActivity);
        return wrappedRelatedActivity;
    }

    @Override
    public TwoPhaseActivity cancelResetPassword(final TwoPhaseActivity relatedActivity) {
        Session currentSession = sessionFactory.getCurrentSession();
        relatedActivity.setArchived(true);
        relatedActivity.setExpirationTime(new DateTime());
        TwoPhaseActivity wrappedRelatedActivity = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, relatedActivity);
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
        AppUserSettings userSettings = getEntitiesForUser(AppUserSettings.class, appUser, 0, 0).iterator().next();
        userSettings.updateSettings(settings);
        sessionFactory.getCurrentSession().update(userSettings);
        saveHistory(userSettings);
        publishUpdate(userSettings);
        return userSettings;
    }

    //  TODO - mark delete and allow undelete
    //  TODO - paginate?
    @Override
    @SuppressWarnings("unchecked")
    public void deleteUser(final AppUser appUser) {
        Session currentSession = sessionFactory.getCurrentSession();

        AppUser wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, appUser);
        wrapped = get(AppUser.class, wrapped.getId());
        if (wrapped == null) {
            return;  //  Already deleted?
        }

        for (Class<? extends AppUserOwnedObject> appUserClass : Arrays.asList(Student.class, ClassList.class, ObservationCategory.class, TwoPhaseActivity.class, AppUserSettings.class)) {
            Collection<? extends AppUserOwnedObject> deleteList = getEntitiesForUser(appUserClass, wrapped, 0, 0);
            for (AppUserOwnedObject entity : deleteList) {
                delete(entity);
            }
        }
        for (DeletedObject deletedObject : getEntitiesForUser(DeletedObject.class, wrapped, 0, 0)) {
            currentSession.delete(deletedObject);
        }
        currentSession.flush();

        Query query = sessionFactory.getCurrentSession().createQuery("delete " + sessionFactory.getClassMetadata(HibernateHistory.class).getEntityName() + " where appUser = :appUser");
        query.setParameter("appUser", wrapped);
        query.executeUpdate();
        currentSession.flush();

        currentSession.delete(wrapped);
        publishDelete(wrapped);
    }

    @Override
    public void deactivateUser(final AppUser user) {
        AppUser wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, user);
        wrapped.setActive(false);

        sessionFactory.getCurrentSession().update(wrapped);
        publishUpdate(wrapped);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> void delete(final T entity) {
        Set<AppUserOwnedObject> updatedItems = new HashSet<>();
        Set<AppUserOwnedObject> deletedItems = new HashSet<>();
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You can not manually delete DeletedObjects.  These are only cleaned up by deleting user.");
        }
        Session currentSession = sessionFactory.getCurrentSession();

        T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        if (wrapped == null) {
            //  Already deleted
            return;
        }

        /*
         *
         *  Note that deleting a semester does not delete observations for semester
         *
         */

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
        final List<Photo> allPhotosForEntity = getAllPhotosForEntity(wrapped, 0, 0);
        for (Photo p : allPhotosForEntity) {
            deletedItems.add(p);
            currentSession.delete(p);
        }
        if (wrapped instanceof Observable) {
            for (Observation o : getAllObservationsForEntity((Observable) wrapped)) {
                final List<Photo> allPhotosForObservation = getAllPhotosForEntity(o, 0, 0);
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
        publishChanges(updatedItems, deletedItems);
    }

    private void dealWithNewObservations(final IdObject idObject) {
        if (idObject instanceof Observation) {
            sessionFactory.getCurrentSession().flush();
            sessionFactory.getCurrentSession().clear();
            Observation observation = get(Observation.class, idObject.getId());
            if (observation.getObservationSubject() == null) {
                throw new RuntimeException("observationSubject is null");
            }
            Observable observable = observation.getObservationSubject();
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

    private <T extends IdObject> void publishCreate(T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.ADDED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.ADDED, wrapped));
            }
        }
    }

    private <T extends IdObject> void publishUpdate(T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.MODIFIED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.MODIFIED, wrapped));
            }
        }
    }

    private void publishChanges(Set<AppUserOwnedObject> updatedItems, Set<AppUserOwnedObject> deletedItems) {
        if (eventBus != null) {
            for (AppUserOwnedObject updatedItem : updatedItems) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.MODIFIED, updatedItem));
            }
            for (AppUserOwnedObject deletedItem : deletedItems) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, deletedItem));
            }
        }
    }

    private <T extends IdObject> void publishDelete(final T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.DELETED, wrapped));
            }
        }
    }
}
