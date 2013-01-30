package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.HibernateAppUserOwnedObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Date: 11/19/12
 * Time: 5:34 PM
 */
@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
@SuppressWarnings("unused")
public class HibernateReadWriteDAO extends HibernateReadOnlyDAO implements ReadWriteDAO {
    @Autowired
    public HibernateReadWriteDAO(final SessionFactory sessionFactory, final DAOIdObjectWrapperFactory wrapperFactory) {
        super(sessionFactory, wrapperFactory);
    }

    @Override
    public <T extends IdObject> T create(final T entity) {
        final T wrapped = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().save(wrapped);
        return wrapped;
    }

    @Override
    public <T extends IdObject> Collection<T> create(final Collection<T> entities) {
        Session session = sessionFactory.getCurrentSession();
        Collection<T> wrappedCollection = wrapperFactory.wrap(entities);
        for (T entity : wrappedCollection) {
            session.save(entity);
        }
        return wrappedCollection;
    }

    @Override
    public <T extends IdObject> T update(final T entity) {
        final T wrapped = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().update(wrapped);
        return wrapped;
    }

    @Override
    public <T extends IdObject> Collection<T> update(final Collection<T> entities) {
        Session session = sessionFactory.getCurrentSession();
        Collection<T> wrappedEntities = wrapperFactory.wrap(entities);
        for (T entity : wrappedEntities) {
            session.update(entity);
        }
        return wrappedEntities;
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

        delete(getEntitiesForUser(HibernateAppUserOwnedObject.class, wrapped));

        currentSession.delete(wrapped);
    }

    @Override
    public void deleteUsers(final Collection<AppUser> users) {
        for (AppUser user : users) {
            deleteUser(user);
        }
    }

    //  TODO - mark delete and allow undelete
    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> void delete(final T entity) {
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
            }
        }
        if (wrapped instanceof ClassList) {
            for (Student student : getAllStudentsForClassList((ClassList) wrapped)) {
                student.removeClassList((ClassList) wrapped);
                currentSession.update(student);
            }
        }
        if (wrapped instanceof Observation) {
            for (Observation observation : getAllObservationsForFollowup((Observation) wrapped)) {
                observation.setFollowUpObservation(null);
                currentSession.update(observation);
            }
        }
        if (wrapped instanceof AppUserOwnedObject) {
            for (Photo p : getAllPhotosForEntity(wrapped)) {
                currentSession.delete(p);
            }
            for (Observation o : getAllObservationsForEntity(wrapped)) {
                currentSession.delete(o);
            }
        }

        currentSession.delete(wrapped);
    }

    @Override
    public <T extends AppUserOwnedObject> void delete(final Collection<T> entities) {
        for (AppUserOwnedObject entity : entities) {
            delete(entity);
        }
    }
}
