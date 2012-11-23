package com.jtbdevelopment.e_eye_o.HibernateDAO;


import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.superclasses.AppUserOwnedObject;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 11/18/12
 * Time: 10:29 PM
 */
@Repository
@SuppressWarnings("unused")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HibernateReadOnlyDAO implements ReadOnlyDAO {

    protected final SessionFactory sessionFactory;

    @Autowired
    public HibernateReadOnlyDAO(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public <T> T get(Class<T> type, String id) {
        return(T) sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, AppUser appUser) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + entityType.getSimpleName() + " where appUser = :user");
        query.setParameter("user", appUser);
        return new HashSet<>( (List<T>) query.list() );
    }

    @Override
    public <T extends ArchivableAppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, AppUser appUser) {
        return loadEntitiesForUserWithArchiveFlag(entityType, appUser, false);
    }

    @Override
    public <T extends ArchivableAppUserOwnedObject> Set<T> getArchivedEntitiesForUser(Class<T> entityType, AppUser appUser) {
        return loadEntitiesForUserWithArchiveFlag(entityType, appUser, true);
    }

    private <T extends ArchivableAppUserOwnedObject> Set<T> loadEntitiesForUserWithArchiveFlag(Class<T> entityType, AppUser appUser, boolean archived) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + entityType.getSimpleName() + " where user = :user and archived = :archived");
        query.setParameter("user", appUser);
        query.setParameter("archived", archived);
        return new HashSet<>( (List<T>) query.list() );
    }
}
