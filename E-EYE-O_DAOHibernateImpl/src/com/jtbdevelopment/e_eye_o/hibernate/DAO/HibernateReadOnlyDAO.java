package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.hibernate.entities.HDBIdObjectWrapperFactory;
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
    protected final HDBIdObjectWrapperFactory wrapperFactory;

    @Autowired
    public HibernateReadOnlyDAO(final SessionFactory sessionFactory, final HDBIdObjectWrapperFactory wrapperFactory) {
        this.sessionFactory = sessionFactory;
        this.wrapperFactory = wrapperFactory;
    }


    @Override
    public <T extends IdObject> T get(final Class<T> type, final String id) {
        Class wrapperFor = wrapperFactory.getWrapperFor(type);
        if (wrapperFor == null) {
            wrapperFor = type;
        }
        return (T) sessionFactory.getCurrentSession().get(wrapperFor, id);
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(Class<T> entityType, AppUser appUser) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + convertImplToEntity(entityType) + " where appUser = :user");
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

    private <T extends ArchivableAppUserOwnedObject> Set<T> loadEntitiesForUserWithArchiveFlag(final Class<T> entityType, final AppUser appUser, final boolean archived) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + convertImplToEntity(entityType) + " where appUser = :user and archived = :archived");
        query.setParameter("user", appUser);
        query.setParameter("archived", archived);
        return new HashSet<>((List<T>) query.list());
    }

    protected String convertImplToEntity(final Class entity) {
        return entity.getSimpleName().replace("Impl", "");
    }
}
