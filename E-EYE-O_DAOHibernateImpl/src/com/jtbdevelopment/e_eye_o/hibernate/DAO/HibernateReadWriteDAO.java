package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ChainedUpdateSetImpl;
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

import java.util.*;

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
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You cannot explicitly create a DeletedObject.");
        }
        final T wrapped = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().save(wrapped);
        return wrapped;
    }

    @Override
    public <T extends IdObject> Collection<T> create(final Collection<T> entities) {
        Session session = sessionFactory.getCurrentSession();
        Collection<T> wrappedCollection = wrapperFactory.wrap(entities);
        for (T entity : wrappedCollection) {
            if (entity instanceof DeletedObject) {
                throw new IllegalArgumentException("You cannot explicitly create a DeletedObject.");
            }
            session.save(entity);
        }
        return wrappedCollection;
    }

    @Override
    public <T extends IdObject> T update(final T entity) {
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You cannot explicitly update a DeletedObject.");
        }
        final T wrapped = wrapperFactory.wrap(entity);
        sessionFactory.getCurrentSession().update(wrapped);
        return wrapped;
    }

    @Override
    public <T extends IdObject> Collection<T> update(final Collection<T> entities) {
        Session session = sessionFactory.getCurrentSession();
        Collection<T> wrappedEntities = wrapperFactory.wrap(entities);
        for (T entity : wrappedEntities) {
            if (entity instanceof DeletedObject) {
                throw new IllegalArgumentException("You cannot explicitly update a DeletedObject.");
            }
            session.update(entity);
        }
        return wrappedEntities;
    }

    //  TODO - mark delete and allow undelete
    @Override
    @SuppressWarnings("unchecked")
    public ChainedUpdateSet<AppUserOwnedObject> deleteUser(final AppUser appUser) {
        Session currentSession = sessionFactory.getCurrentSession();

        AppUser wrapped = wrapperFactory.wrap(appUser);
        wrapped = get(AppUser.class, wrapped.getId());
        if (wrapped == null) {
            return new ChainedUpdateSetImpl<AppUserOwnedObject>(Collections.EMPTY_SET, Collections.EMPTY_SET);  //  Already deleted?
        }

        Map<HibernateAppUserOwnedObject, ChainedUpdateSet<AppUserOwnedObject>> returnSet
                = delete(getEntitiesForUser(HibernateAppUserOwnedObject.class, wrapped));
        for (DeletedObject deletedObject : getEntitiesForUser(DeletedObject.class, wrapped)) {
            currentSession.delete(deletedObject);
        }

        currentSession.delete(wrapped);
        //  TODO - tess
        return new ChainedUpdateSetImpl<>(returnSet.values());
    }

    @Override
    public Map<AppUser, ChainedUpdateSet<AppUserOwnedObject>> deleteUsers(final Collection<AppUser> users) {
        Map<AppUser, ChainedUpdateSet<AppUserOwnedObject>> results = new HashMap<>();
        for (AppUser user : users) {
            results.put(user, deleteUser(user));
        }
        //  TODO - tests
        return results;
    }

    //  TODO - mark delete and allow undelete
    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> ChainedUpdateSet<AppUserOwnedObject> delete(final T entity) {
        Set<AppUserOwnedObject> updatedItems = new HashSet<>();
        Set<AppUserOwnedObject> deletedItems = new HashSet<>();
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You can not manually delete DeletedObjects.  These are only cleaned up by deleting user.");
        }
        Session currentSession = sessionFactory.getCurrentSession();

        T wrapped = wrapperFactory.wrap(entity);
        wrapped = (T) get(wrapped.getClass(), wrapped.getId());
        if (wrapped == null) {
            //  Already deleted
            return new ChainedUpdateSetImpl<>(updatedItems, deletedItems);
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
        final List<Photo> allPhotosForEntity = getAllPhotosForEntity(wrapped);
        for (Photo p : allPhotosForEntity) {
            deletedItems.add(p);
            currentSession.delete(p);
        }
        for (Observation o : getAllObservationsForEntity(wrapped)) {
            deletedItems.add(o);
            currentSession.delete(o);
        }

        deletedItems.add(wrapped);
        currentSession.delete(wrapped);
        currentSession.flush();   //  Force any delete object generations inside this session
        //  TODO - unit test adjustments for deletedItems verification
        return new ChainedUpdateSetImpl<>(updatedItems, deletedItems);
    }

    @Override
    public <T extends AppUserOwnedObject> Map<T, ChainedUpdateSet<AppUserOwnedObject>> delete(final Collection<T> entities) {
        Map<T, ChainedUpdateSet<AppUserOwnedObject>> deletedItems = new HashMap<>();
        for (T entity : entities) {
            deletedItems.put(entity, delete(entity));
        }
        //  TODO - verify
        return deletedItems;
    }
}
