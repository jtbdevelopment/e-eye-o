package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.IdObjectFieldUpdateValidator;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.events.EventFactory;
import com.jtbdevelopment.e_eye_o.entities.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Date: 11/19/12
 * Time: 5:34 PM
 */
@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
@SuppressWarnings("unused")
public class HibernateReadWriteDAO extends HibernateReadOnlyDAO implements ReadWriteDAO {

    private final IdObjectFieldUpdateValidator idObjectFieldUpdateValidator;
    private final EventBus eventBus;
    private final EventFactory eventFactory;

    @Autowired
    public HibernateReadWriteDAO(final EventBus eventBus, final EventFactory eventFactory, final SessionFactory sessionFactory, final IdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper, final IdObjectFieldUpdateValidator idObjectFieldUpdateValidator, final IdObjectFactory idObjectFactory) {
        super(sessionFactory, wrapperFactory, idObjectReflectionHelper, idObjectFactory);
        this.idObjectFieldUpdateValidator = idObjectFieldUpdateValidator;
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
        idObjectFieldUpdateValidator.removeInvalidFieldUpdates(updatingUser, existing, entity);
        sessionFactory.getCurrentSession().clear();
        T saved = trustedUpdate(entity);
        dealWithObservationChanges(saved, false);
        return saved;
    }

    @Override
    public <T extends IdObject> T trustedUpdate(final T entity) {
        final T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        entity.setModificationTimestamp(DateTime.now());
        sessionFactory.getCurrentSession().update(wrapped);
        publishUpdate(wrapped);
        return (T) get(wrapped.getClass(), wrapped.getId());
    }

    @Override
    public <T extends IdObject> List<T> trustedUpdates(final Collection<T> entities) {
        List<T> returns = new LinkedList<>();
        for (T entity : entities) {
            returns.add(trustedUpdate(entity));
        }
        return returns;
    }

    @Override
    public <T extends IdObject> void trustedDelete(final T entity) {
        final T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        sessionFactory.getCurrentSession().delete(wrapped);
        dealWithObservationChanges(wrapped, true);
        publishDelete(wrapped);
    }

    @Override
    public AppUser updateAppUserLogout(final AppUser appUser) {
        AppUser loaded = get(AppUser.class, appUser.getId());
        loaded.setLastLogout(new DateTime());
        return trustedUpdate(loaded);
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
                return trustedUpdate(observable);
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
