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
        final T t = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().save(t);
        return t;
    }

    @Override
    public <T extends IdObject> T update(final T entity) {
        final T t = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().update(t);
        return t;
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
    public <T extends IdObject> Collection<T> update(final Collection<T> entities) {
        Session session = sessionFactory.getCurrentSession();
        Collection<T> wrappedCollection = wrapperFactory.wrap(entities);
        for (T entity : wrappedCollection) {
            session.update(entity);
        }
        return wrappedCollection;
    }

    //  TODO - mark delete and allow undelete
    @Override
    public void deleteUser(final AppUser paramUser) {
        Session currentSession = sessionFactory.getCurrentSession();

        AppUser user = wrapperFactory.wrap(paramUser);
        user = get(AppUser.class, user.getId());
        if(user == null) {
            return;  //  Already deleted?
        }

        Query query = currentSession.createQuery("from AppUserOwnedObject where appUser = :user");
        query.setParameter("user", user);
        delete((List<AppUserOwnedObject>) query.list());

        currentSession.delete(user);
    }

    @Override
    public void deleteUsers(final Iterable<AppUser> users) {
        for (AppUser user : users) {
            deleteUser(user);
        }
    }

    //  TODO - mark delete and allow undelete
    @Override
    public <T extends AppUserOwnedObject> void delete(final T paramEntity) {
        Session currentSession = sessionFactory.getCurrentSession();

        T entity = wrapperFactory.wrap(paramEntity);
        entity = (T) get(entity.getClass(), entity.getId());
        if(entity == null) {
            //  Already deleted
            return;
        }

        if (entity instanceof ObservationCategory) {
            Query query = currentSession.createQuery("from Observation as O where :category member of O.categories");
            query.setParameter("category", entity);
            for (Observation observation : (List<Observation>) query.list()) {
                observation.removeCategory((ObservationCategory) entity);
                currentSession.update(observation);
            }
        } else if ( entity instanceof ClassList) {
            Query query = currentSession.createQuery("from Student as S where :classList member of S.classLists");
            query.setParameter("classList", entity);
            for (Student student : (List<Student>) query.list()) {
                student.removeClassList((ClassList) entity);
                currentSession.update(student);
            }
        }
        if (entity instanceof AppUserOwnedObject) {
            Query query = currentSession.createQuery("from Photo where photoFor = :photoFor");
            query.setParameter("photoFor", entity);
            delete((List<Photo>)query.list());
            query = currentSession.createQuery("from Observation where observationSubject = :observationSubject");
            query.setParameter("observationSubject", entity);
            delete((List<Observation>)query.list());
        }

        currentSession.delete(entity);
    }

    @Override
    public <T extends AppUserOwnedObject> void delete(final Collection<T> entities) {
        for (AppUserOwnedObject entity : entities) {
            delete(entity);
        }
    }
}
