package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.HibernateIdObject;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Date: 11/18/12
 * Time: 10:29 PM
 */
@SuppressWarnings("unused")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HibernateReadOnlyDAO implements ReadOnlyDAO {

    private final static Logger logger = LoggerFactory.getLogger(HibernateReadOnlyDAO.class);

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
        final String entityName = getHibernateEntityName(entityType);
        logger.info("Fetching entityType = '" + entityName + "', id = '" + id + "'");
        return (T) sessionFactory.getCurrentSession().get(entityName, id);
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
        return getEntitiesForUserWithArchiveFlag(entityType, appUser, false);
    }

    @Override
    public <T extends ArchivableAppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType, final AppUser appUser) {
        return getEntitiesForUserWithArchiveFlag(entityType, appUser, true);
    }

    @SuppressWarnings("unchecked")
    private <T extends ArchivableAppUserOwnedObject> Set<T> getEntitiesForUserWithArchiveFlag(final Class<T> entityType, final AppUser appUser, final boolean archived) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + getHibernateEntityName(entityType) + " where appUser = :user and archived = :archived");
        query.setParameter("user", appUser);
        query.setParameter("archived", archived);
        return new HashSet<>((List<T>) query.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Photo where photoFor = :photoFor");
        query.setParameter("photoFor", ownedObject);
        return (List<Photo>) query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForEntity(final AppUserOwnedObject ownedObject) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Observation where observationSubject = :observationSubject");
        query.setParameter("observationSubject", ownedObject);
        return (List<Observation>) query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForObservationCategory(final ObservationCategory observationCategory) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Observation as O where :category member of O.categories");
        query.setParameter("category", observationCategory);
        return (List<Observation>) query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForFollowup(final Observation followup) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Observation as O where followUpObservation = :followUpObservation");
        query.setParameter("followUpObservation", followup);
        return (List<Observation>) query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> getAllStudentsForClassList(final ClassList classList) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Student as S where :classList member of S.classLists");
        query.setParameter("classList", classList);
        return (List<Student>) query.list();
    }

    protected <T extends IdObject> String getHibernateEntityName(final Class<T> entityType) {
        Class<T> wrapperFor;
        if (!HibernateIdObject.class.isAssignableFrom(entityType)) {
            wrapperFor = wrapperFactory.getWrapperForEntity(entityType);
            if( wrapperFor == null) {
                throw new IllegalArgumentException("Unknown entity type " + entityType.getSimpleName());
            }
        } else {
            wrapperFor = entityType;
        }

        return sessionFactory.getClassMetadata(wrapperFor).getEntityName();
    }
}
