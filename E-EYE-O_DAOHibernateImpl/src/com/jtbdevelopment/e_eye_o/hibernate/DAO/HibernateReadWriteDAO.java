package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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
    public void deleteUser(final AppUser appUser) {
        Session currentSession = sessionFactory.getCurrentSession();

        AppUser wrapped = wrapperFactory.wrap(appUser);
        wrapped = get(AppUser.class, wrapped.getId());
        if (wrapped == null) {
            return;  //  Already deleted?
        }

        Query query = currentSession.createQuery("from AppUserOwnedObject where appUser = :appUser");
        query.setParameter("appUser", wrapped);
        delete((List<AppUserOwnedObject>) query.list());

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
    public <T extends AppUserOwnedObject> void delete(final T entity) {
        Session currentSession = sessionFactory.getCurrentSession();

        T wrapped = wrapperFactory.wrap(entity);
        wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        if (wrapped == null) {
            //  Already deleted
            return;
        }

        if (wrapped instanceof ObservationCategory) {
            Query query = currentSession.createQuery("from Observation as O where :category member of O.categories");
            query.setParameter("category", wrapped);
            for (Observation observation : (List<Observation>) query.list()) {
                observation.removeCategory((ObservationCategory) wrapped);
                currentSession.update(observation);
            }
        }
        if (wrapped instanceof ClassList) {
            Query query = currentSession.createQuery("from Student as S where :classList member of S.classLists");
            query.setParameter("classList", wrapped);
            for (Student student : (List<Student>) query.list()) {
                student.removeClassList((ClassList) wrapped);
                currentSession.update(student);
            }
        }
        if (wrapped instanceof Observation) {
            Query query = currentSession.createQuery("from Observation as O where followUpObservation = :followUpObservation");
            query.setParameter("followUpObservation", wrapped);
            for (Observation observation : (List<Observation>) query.list()) {
                observation.setFollowUpObservation(null);
                currentSession.update(observation);
            }
        }
        if (wrapped instanceof AppUserOwnedObject) {
            Query query = currentSession.createQuery("from Photo where photoFor = :photoFor");
            query.setParameter("photoFor", wrapped);
            for (Photo p : (List<Photo>) query.list()) {
                currentSession.delete(p);
            }
            query = currentSession.createQuery("from Observation where observationSubject = :observationSubject");
            query.setParameter("observationSubject", wrapped);
            for (Observation o : (List<Observation>) query.list()) {
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
