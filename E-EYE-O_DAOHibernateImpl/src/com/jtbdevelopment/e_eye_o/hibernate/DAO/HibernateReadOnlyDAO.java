package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
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
    protected final DAOIdObjectWrapperFactory wrapperFactory;

    @Autowired
    public HibernateReadOnlyDAO(final SessionFactory sessionFactory, final DAOIdObjectWrapperFactory wrapperFactory) {
        this.sessionFactory = sessionFactory;
        this.wrapperFactory = wrapperFactory;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T get(final Class<T> entityType, final String id) {
        return (T) sessionFactory.getCurrentSession().get(getHibernateEntityName(entityType), id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, AppUser appUser) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + getHibernateEntityName(entityType) + " where appUser = :user");
        query.setParameter("user", appUser);
        return new HashSet<>((List<T>) query.list());
    }

    @Override
    public <T extends ArchivableAppUserOwnedObject> Set<T> getActiveEntitiesForUser(Class<T> entityType, AppUser appUser) {
        return loadEntitiesForUserWithArchiveFlag(entityType, appUser, false);
    }

    @Override
    public <T extends ArchivableAppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType, final AppUser appUser) {
        return loadEntitiesForUserWithArchiveFlag(entityType, appUser, true);
    }

    @SuppressWarnings("unchecked")
    private <T extends ArchivableAppUserOwnedObject> Set<T> loadEntitiesForUserWithArchiveFlag(final Class<T> entityType, final AppUser appUser, final boolean archived) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + getHibernateEntityName(entityType) + " where appUser = :user and archived = :archived");
        query.setParameter("user", appUser);
        query.setParameter("archived", archived);
        return new HashSet<>((List<T>) query.list());
    }

    protected <T extends IdObject> String getHibernateEntityName(final Class<T> entityType) {
        Class<T> wrapperFor = wrapperFactory.getWrapperForEntity(entityType);
        if (wrapperFor == null) {
            wrapperFor = entityType;
        }
        return sessionFactory.getClassMetadata(wrapperFor).getEntityName();
    }

}
