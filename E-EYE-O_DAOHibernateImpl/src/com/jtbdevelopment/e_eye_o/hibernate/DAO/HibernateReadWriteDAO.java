package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.hibernate.entities.HDBAppUser;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HDBIdObjectWrapperFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
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
    private final HDBIdObjectWrapperFactory wrapperFactory;

    @Autowired
    public HibernateReadWriteDAO(final SessionFactory sessionFactory, final HDBIdObjectWrapperFactory wrapperFactory) {
        super(sessionFactory);
        this.wrapperFactory = wrapperFactory;
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
        for (T entity : entities) {
            entities.remove(entity);
            entities.add(create(entity));
        }
        return entities;
    }

    @Override
    public <T extends IdObject> Collection<T> update(final Collection<T> entities) {
        for (T entity : entities) {
            update(entity);
        }
        return entities;
    }

    //  TODO - mark delete and allow undelete
    @Override
    public void deleteUser(final AppUser paramUser) {
        AppUser user = wrapperFactory.wrap(paramUser);
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("from ClassList where appUser = :user");
        query.setParameter("user", user);
        currentSession.delete((List<ClassList>) query.list());
        query = currentSession.createQuery("from Student where appUser = :user");
        query.setParameter("user", user);
        currentSession.delete((List<Student>) query.list());
        query = currentSession.createQuery("from Observation where appUser = :user");
        query.setParameter("user", user);
        currentSession.delete((List<Observation>) query.list());
        query = currentSession.createQuery("from ObservationCategory where appUser = :user");
        query.setParameter("user", user);
        currentSession.delete((List<ObservationCategory>) query.list());
        query = currentSession.createQuery("from Photo where appUser = :user");
        query.setParameter("user", user);
        currentSession.delete((List<Photo>) query.list());
        currentSession.delete(get(HDBAppUser.class, user.getId()));
    }

    @Override
    public void deleteUsers(final Iterable<AppUser> users) {
        for (AppUser user : users) {
            deleteUser(user);
        }
    }

    //  TODO - mark delete and allow undelete
    //  TODO - messy - better modelling would fix?
    @Override
    public <T extends AppUserOwnedObject> void delete(final T paramEntity) {
        T entity = wrapperFactory.wrap(paramEntity);
        Session currentSession = sessionFactory.getCurrentSession();
        Query query;
        if (entity instanceof Student) {
            query = currentSession.createQuery("from ClassList as CL where :student member of CL.students");
            query.setParameter("student", entity);
            for (ClassList classList : (List<ClassList>) query.list()) {
                classList.removeStudent((Student) entity);
            }
        } else if (entity instanceof Observation) {
            query = currentSession.createQuery("from Student as S where :observation member of S.observations");
            query.setParameter("observation", entity);
            for (Student student : (List<Student>) query.list()) {
                student.removeObservation((Observation) entity);
            }
        } else if (entity instanceof ObservationCategory) {
            query = currentSession.createQuery("from Observation as O where :category member of O.categories");
            query.setParameter("category", entity);
            for (Observation observation : (List<Observation>) query.list()) {
                observation.removeCategory((ObservationCategory) entity);
            }
        } else if (entity instanceof Photo) {
            query = currentSession.createQuery("from ClassList as CL where :photo member of CL.photos");
            query.setParameter("photo", entity);
            for (ClassList classList : (List<ClassList>) query.list()) {
                classList.removePhoto((Photo) entity);
            }
            query = currentSession.createQuery("from Observation as O where :photo member of O.photos");
            query.setParameter("photo", entity);
            for (Observation observation : (List<Observation>) query.list()) {
                observation.removePhoto((Photo) entity);
            }
            query = currentSession.createQuery("from Student where studentPhoto = :photo");
            query.setParameter("photo", entity);
            for (Student student : (List<Student>) query.list()) {
                student.setStudentPhoto(null);
            }
        } else if (entity instanceof ClassList) {
            //
        } else {
            throw new InvalidParameterException("Do not know how to delete type " + entity.getClass().getSimpleName());
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
