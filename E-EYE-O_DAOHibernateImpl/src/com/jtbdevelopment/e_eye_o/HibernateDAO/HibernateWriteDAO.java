package com.jtbdevelopment.e_eye_o.HibernateDAO;

import com.jtbdevelopment.e_eye_o.DAO.WriteDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.superclasses.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.superclasses.IdObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date: 11/19/12
 * Time: 5:34 PM
 */
@Repository
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
@SuppressWarnings("unused")
public class HibernateWriteDAO extends HibernateReadOnlyDAO implements WriteDAO {
    @Autowired
    public HibernateWriteDAO(final SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public <T extends IdObject> T create(T entity) {
        sessionFactory.getCurrentSession().save(entity);
        return entity;
    }

    @Override
    public <T extends IdObject> T update(T entity) {
        sessionFactory.getCurrentSession().update(entity);
        return entity;
    }

    @Override
    public Iterable<? extends IdObject> create(Iterable<? extends  IdObject> entities) {
        Session session = sessionFactory.getCurrentSession();
        for (Object entity : entities) {
            session.save(entity);
        }
        return entities;
    }

    @Override
    public Iterable<? extends IdObject> update(Iterable<? extends IdObject> entities) {
        Session session = sessionFactory.getCurrentSession();
        for (Object entity : entities) {
            session.update(entity);
        }
        return entities;
    }

    //  TODO - mark delete and allow undelete
    @Override
    public void deleteUser(AppUser user) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query = currentSession.createQuery("from ClassList where appUser = :user");
        query.setParameter("user", user);
        delete((List<ClassList>) query.list());
        query = currentSession.createQuery("from Student where appUser = :user");
        query.setParameter("user", user);
        delete((List<Student>) query.list());
        query = currentSession.createQuery("from Observation where appUser = :user");
        query.setParameter("user", user);
        delete((List<Observation>) query.list());
        query = currentSession.createQuery("from ObservationCategory where appUser = :user");
        query.setParameter("user", user);
        delete((List<ObservationCategory>) query.list());
        query = currentSession.createQuery("from Photo where appUser = :user");
        query.setParameter("user", user);
        delete((List<Photo>) query.list());
        currentSession.delete(get(AppUser.class,  user.getId()));
    }

    @Override
    public void deleteUsers(Iterable<AppUser> users) {
        for(AppUser user : users) {
            deleteUser(user);
        }
    }

    //  TODO - mark delete and allow undelete
    //  TODO - messy - better modelling would fix?
    @Override
    public <T extends AppUserOwnedObject> void delete(T entity) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query query;
        switch (entity.getClass().getSimpleName()) {
            case "Student":
                query = currentSession.createQuery("from ClassList as CL where :student member of CL.students");
                query.setParameter("student", entity);
                for(ClassList classList : (List<ClassList>) query.list()) {
                    classList.removeStudent((Student) entity);
                }
                break;
            case "Observation":
                query = currentSession.createQuery("from Student as S where :observation member of S.observations");
                query.setParameter("observation", entity);
                for(Student student : (List<Student>) query.list()) {
                    student.removeObservation((Observation) entity);
                }
                break;
            case "ObservationCategory":
                query = currentSession.createQuery("from Observation as O where :category member of O.categories");
                query.setParameter("category", entity);
                for(Observation observation : (List<Observation>) query.list()) {
                    observation.removeCategory((ObservationCategory) entity);
                }
                break;
            case "Photo":
                query = currentSession.createQuery("from ClassList as CL where :photo member of CL.photos");
                query.setParameter("photo", entity);
                for(ClassList classList : (List<ClassList>) query.list()) {
                    classList.removePhoto((Photo) entity);
                }
                query = currentSession.createQuery("from Observation as O where :photo member of O.photos");
                query.setParameter("photo", entity);
                for(Observation observation : (List<Observation>) query.list()) {
                    observation.removePhoto((Photo) entity);
                }
                query = currentSession.createQuery("from Student where studentPhoto = :photo");
                query.setParameter("photo", entity);
                for(Student student : (List<Student>) query.list()) {
                    student.setStudentPhoto(null);
                }
                break;
        }
        currentSession.delete(entity);
    }

    @Override
    public void delete(Iterable<? extends AppUserOwnedObject> entities) {
        Session session = sessionFactory.getCurrentSession();
        for (Object entity : entities) {
            session.delete(entity);
        }
    }
}
