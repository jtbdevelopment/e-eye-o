package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 3/30/13
 * Time: 10:14 AM
 */
@Component
@SuppressWarnings("unused")
public class ObservableLastObservationGenerator implements PostDeleteEventListener, PostUpdateEventListener, PostInsertEventListener {
    private final SessionFactory sessionFactory;
    private final ReadOnlyDAO readOnlyDAO;

    @Autowired
    public ObservableLastObservationGenerator(final ReadOnlyDAO readOnlyDAO, final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.readOnlyDAO = readOnlyDAO;
    }

    @PostConstruct
    public void registerMe() {
        if (sessionFactory instanceof SessionFactoryImpl) {
            ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class).appendListeners(EventType.POST_DELETE, this);
            ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class).appendListeners(EventType.POST_INSERT, this);
            ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService(EventListenerRegistry.class).appendListeners(EventType.POST_UPDATE, this);
        }
    }

    @Override
    public void onPostInsert(final PostInsertEvent event) {
        if (event.getEntity() instanceof Observation) {
            Observation observation = (Observation) event.getEntity();
            Observable observationSubject = readOnlyDAO.get(Observable.class, observation.getObservationSubject().getId());
            if (observation.getObservationTimestamp().compareTo(observationSubject.getLastObservationTime()) > 0) {
                observationSubject.setLastObservationTime(observation.getObservationTimestamp());
                observationSubject.setModificationTimestamp(new DateTime());
                event.getSession().update(observationSubject);
            }
        }
    }

    //  TODO - update is more complicated than delete or insert - this is simple but not efficient
    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity() instanceof Observation) {
            Observation observation = (Observation) event.getEntity();
            Observable observationSubject = readOnlyDAO.get(Observable.class, observation.getObservationSubject().getId());
            LocalDateTime lastObservationTimestampForEntity = readOnlyDAO.getLastObservationTimestampForEntity(observation.getObservationSubject());
            if (lastObservationTimestampForEntity.compareTo(observationSubject.getLastObservationTime()) != 0) {
                observationSubject.setLastObservationTime(lastObservationTimestampForEntity);
                observationSubject.setModificationTimestamp(new DateTime());
                event.getSession().update(observationSubject);
            }
        }
    }

    @Override
    public void onPostDelete(PostDeleteEvent event) {
        if (event.getEntity() instanceof Observation) {
            Observation observation = (Observation) event.getEntity();
            Observable observationSubject = readOnlyDAO.get(Observable.class, observation.getObservationSubject().getId());
            if (observation.getObservationTimestamp().compareTo(observationSubject.getLastObservationTime()) == 0) {
                observationSubject.setLastObservationTime(readOnlyDAO.getLastObservationTimestampForEntity(observation.getObservationSubject()));
                observationSubject.setModificationTimestamp(new DateTime());
                event.getSession().update(observationSubject);
            }
        }
    }
}