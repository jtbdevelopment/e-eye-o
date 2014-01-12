package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.FieldUpdateValidator;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.events.EventFactory;
import com.jtbdevelopment.e_eye_o.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.HibernateObservation;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import org.hibernate.Query;
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

    private final FieldUpdateValidator fieldUpdateValidator;
    private final EventBus eventBus;
    private final EventFactory eventFactory;

    @Autowired
    public HibernateReadWriteDAO(final EventBus eventBus, final EventFactory eventFactory, final SessionFactory sessionFactory, final IdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper, final FieldUpdateValidator fieldUpdateValidator, final IdObjectFactory idObjectFactory) {
        super(sessionFactory, wrapperFactory, idObjectReflectionHelper, idObjectFactory);
        this.fieldUpdateValidator = fieldUpdateValidator;
        this.eventBus = eventBus;
        this.eventFactory = eventFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T create(final T entity) {
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You cannot explicitly create a DeletedObject.");
        }
        final T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        wrapped.setModificationTimestamp(DateTime.now());
        dealWithObservationChanges(wrapped, false);
        sessionFactory.getCurrentSession().save(wrapped);
        publishCreate(wrapped);
        return (T) get(wrapped.getClass(), wrapped.getId());
    }

    @Override
    public <T extends IdObject> T update(final AppUser updatingUser, final T entity) {
        if (!idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass()).getAnnotation(IdObjectEntitySettings.class).editable()) {
            throw new IllegalArgumentException("You cannot explicitly update a " + entity.getClass() + ".");
        }
        @SuppressWarnings("unchecked")
        final T existing = get((Class<T>) entity.getClass(), entity.getId());
        fieldUpdateValidator.removeInvalidFieldUpdates(updatingUser, existing, entity);
        dealWithObservationChanges(entity, false);
        T saved = trustedUpdate(entity);
        return saved;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T trustedUpdate(final T entity) {
        final T wrapped = wrapperFactory.wrap(IdObjectWrapperFactory.WrapperKind.DAO, entity);
        entity.setModificationTimestamp(DateTime.now());
        sessionFactory.getCurrentSession().merge(wrapped);
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
            Observable observable = get(Observable.class, observation.getObservationSubject().getId());
            if (observable == null) {
                return null;
            }
            Query query = sessionFactory.
                    getCurrentSession().createQuery(
                    "select id, observationTimestamp from " +
                            getHibernateEntityName(HibernateObservation.class) +
                            " where observationSubject = :observationSubject");
            query.setParameter("observationSubject", observable);
            List<Object[]> result = query.list();
            Map<String, LocalDateTime> dates = new HashMap<>();

            for (Object[] o : result) {
                dates.put((String) o[0], (LocalDateTime) o[1]);
            }
            dates.remove(observable.getId());
            if (!isDelete) {
                dates.put(observation.getId(), observation.getObservationTimestamp());
            }
            LocalDateTime last = Observable.NEVER_OBSERVED;
            for (LocalDateTime dateTime : dates.values()) {
                if (last.compareTo(dateTime) < 0) {
                    last = dateTime;
                }
            }

            if (!observable.getLastObservationTimestamp().equals(last)) {
                observable.setLastObservationTimestamp(last);
                return trustedUpdate(observable);
            }
        }
        return null;
    }

    public <T extends IdObject> void publishCreate(T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.ADDED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.ADDED, wrapped));
            }
        }
    }

    public <T extends IdObject> void publishUpdate(T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.UPDATED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.UPDATED, wrapped));
            }
        }
    }

    public <T extends IdObject> void publishDelete(final T wrapped) {
        if (eventBus != null) {
            if (wrapped instanceof AppUserOwnedObject) {
                eventBus.post(eventFactory.newAppUserOwnedObjectChanged(IdObjectChanged.ChangeType.DELETED, (AppUserOwnedObject) wrapped));
            } else {
                eventBus.post(eventFactory.newIdObjectChanged(IdObjectChanged.ChangeType.DELETED, wrapped));
            }
        }
    }
}
