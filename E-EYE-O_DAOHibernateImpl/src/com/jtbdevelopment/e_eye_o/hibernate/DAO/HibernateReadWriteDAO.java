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
    public HibernateReadWriteDAO(final EventBus eventBus, final EventFactory eventFactory, final SessionFactory sessionFactory, final IdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper, final IdObjectUpdateHelper idObjectUpdateHelper, final IdObjectFactory idObjectFactory) {
        super(sessionFactory, wrapperFactory, idObjectReflectionHelper, idObjectFactory);
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
        wrapped.setModificationTimestamp(DateTime.now());
        sessionFactory.getCurrentSession().save(wrapped);
        dealWithObservationChanges(wrapped, false);
        publishCreate(wrapped);
        return (T) get(wrapped.getClass(), wrapped.getId());
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
        entity.setModificationTimestamp(DateTime.now());
        sessionFactory.getCurrentSession().update(wrapped);
        dealWithObservationChanges(entity, false);
        publishUpdate(wrapped);
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
        AppUser appUser = get(AppUser.class, relatedActivity.getAppUser().getId());
        appUser.setActive(true);
        appUser.setActivated(true);
        internalUpdate(appUser);
        relatedActivity.setArchived(true);
        return internalUpdate(relatedActivity);
    }

    @Override
    public TwoPhaseActivity updateUserEmailAddress(final TwoPhaseActivity changeRequest, final String newAddress) {
        AppUser appUser = get(AppUser.class, changeRequest.getAppUser().getId());
        appUser.setEmailAddress(newAddress);
        internalUpdate(appUser);
        changeRequest.setArchived(true);
        return internalUpdate(changeRequest);
    }

    @Override
    public TwoPhaseActivity resetUserPassword(final TwoPhaseActivity relatedActivity, final String newPassword) {
        AppUser appUser = get(AppUser.class, relatedActivity.getAppUser().getId());
        appUser.setPassword(newPassword);
        internalUpdate(appUser);
        relatedActivity.setArchived(true);
        return internalUpdate(relatedActivity);
    }

    @Override
    public TwoPhaseActivity cancelResetPassword(final TwoPhaseActivity relatedActivity) {
        relatedActivity.setArchived(true);
        relatedActivity.setExpirationTime(new DateTime());
        return internalUpdate(relatedActivity);
    }

    @Override
    public AppUser updateAppUserLogout(final AppUser appUser) {
        AppUser loaded = get(AppUser.class, appUser.getId());
        loaded.setLastLogout(new DateTime());
        return internalUpdate(loaded);
    }

    @Override
    public void deactivateUser(final AppUser user) {
        AppUser loaded = get(AppUser.class, user.getId());
        loaded.setActive(false);
        internalUpdate(loaded);
    }

    @Override
    public AppUserSettings updateSettings(final AppUser appUser, final Map<String, Object> settings) {
        AppUserSettings userSettings = getEntitiesForUser(AppUserSettings.class, appUser, 0, 0).iterator().next();
        userSettings.updateSettings(settings);
        return internalUpdate(userSettings);
    }

    //  TODO - allow undelete - feasible now with envers
    @Override
    @SuppressWarnings("unchecked")
    public void deleteUser(final AppUser appUser) {
        Session currentSession = sessionFactory.getCurrentSession();

        AppUser wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, appUser);
        wrapped = get(AppUser.class, wrapped.getId());
        if (wrapped == null) {
            return;  //  Already deleted?
        }

        for (Class<? extends AppUserOwnedObject> appUserClass : Arrays.asList(Student.class, ClassList.class, ObservationCategory.class, Semester.class, TwoPhaseActivity.class, AppUserSettings.class)) {
            Collection<? extends AppUserOwnedObject> deleteList = getEntitiesForUser(appUserClass, wrapped, 0, 0);
            for (AppUserOwnedObject entity : deleteList) {
                delete(entity);
            }
        }

        //  TODO - delete audit tables

        currentSession.delete(wrapped);
        publishDelete(wrapped);
    }

    @Override
    @SuppressWarnings("unchecked")
    //  TODO - allow undelete - feasible now with envers
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
                updatedItems.add(internalUpdate(observation));
            }
        }
        if (wrapped instanceof ClassList) {
            for (Student student : getAllStudentsForClassList((ClassList) wrapped)) {
                student.removeClassList((ClassList) wrapped);
                updatedItems.add(internalUpdate(student));
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
        Observable observable = dealWithObservationChanges(entity, true);
        if (observable != null) {
            updatedItems.add(observable);  //  Flush forced internally
        }
        publishChanges(updatedItems, deletedItems);
    }

    private Observable dealWithObservationChanges(final IdObject entity, final boolean isDelete) {
        if (entity instanceof Observation) {
            Observation observation = (Observation) entity;
            Observable observable = get(Observable.class, ((Observation) entity).getObservationSubject().getId());
            if (observable == null) {
                return null;
            }
            Set<Observation> observationSet = getEntitiesForUser(Observation.class, observation.getAppUser(), 0, 0);
            if (isDelete) {
                observationSet.remove(observation);
            } else {
                observationSet.add(observation);
            }
            LocalDateTime last = Observable.NEVER_OBSERVED;
            for (Observation o : observationSet) {
                if (last.compareTo(o.getObservationTimestamp()) < 0) {
                    last = o.getObservationTimestamp();
                }
            }
            if (!observable.getLastObservationTimestamp().equals(last)) {
                observable.setLastObservationTimestamp(last);
                return internalUpdate(observable);
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
