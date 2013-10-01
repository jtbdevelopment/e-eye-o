package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Date: 11/18/12
 * Time: 10:29 PM
 */
@SuppressWarnings("unused")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class HibernateReadOnlyDAO implements ReadOnlyDAO {
    private final static Logger logger = LoggerFactory.getLogger(HibernateReadOnlyDAO.class);

    protected final IdObjectReflectionHelper idObjectReflectionHelper;
    protected final SessionFactory sessionFactory;
    protected final DAOIdObjectWrapperFactory wrapperFactory;

    @Autowired
    public HibernateReadOnlyDAO(final SessionFactory sessionFactory, final DAOIdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper) {
        this.sessionFactory = sessionFactory;
        this.wrapperFactory = wrapperFactory;
        this.idObjectReflectionHelper = idObjectReflectionHelper;
    }


    @Override
    //  TODO - paging
    @SuppressWarnings("unchecked")
    public Set<AppUser> getUsers() {
        Set<AppUser> returnSet = new LinkedHashSet<>();
        returnSet.addAll((List<AppUser>) sessionFactory.getCurrentSession().createCriteria(HibernateAppUser.class).addOrder(Order.asc("id")).list());
        return returnSet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppUser getUser(final String emailAddress) {
        return (AppUser) sessionFactory.getCurrentSession().createCriteria(HibernateAppUser.class)
                .add(Restrictions.eq("emailAddress", emailAddress))
                .uniqueResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T get(final Class<T> entityType, final String id) {
        final String entityName = getHibernateEntityName(entityType);
        logger.info("Fetching entityType = '" + entityName + "', id = '" + id + "'");
        return (T) sessionFactory.getCurrentSession().get(entityName, id);
    }

    private int returnRowCountForCriteria(final Criteria criteria) {
        criteria.setProjection(Projections.rowCount());
        return ((Number) criteria.uniqueResult()).intValue();
    }

    private <T extends AppUserOwnedObject> Criteria createCriteria(Class<T> entityType, AppUser appUser, int firstResult, int maxResults) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(getHibernateEntityName(entityType))
                .add(Restrictions.eq("appUser", appUser))
                .addOrder(Order.asc("id"));
        if (maxResults > 0) {
            criteria.setFirstResult(firstResult);
            criteria.setMaxResults(maxResults);
        }
        return criteria;
    }

    private <T extends AppUserOwnedObject> Criteria createCriteriaWithArchiveFlag(Class<T> entityType, AppUser appUser, boolean archivedFlag, int firstResult, int maxResults) {
        Criteria criteria = createCriteria(entityType, appUser, firstResult, maxResults);
        criteria.add(Restrictions.eq("archived", archivedFlag));
        return criteria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, int firstResult, int maxResults) {
        Criteria criteria = createCriteria(entityType, appUser, firstResult, maxResults);
        return new HashSet<>((List<T>) criteria.list());
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(final Class<T> entityType, final AppUser appUser, int firstResult, int maxResults) {
        return getEntitiesForUser(entityType, appUser, false, firstResult, maxResults);
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType, final AppUser appUser, int firstResult, int maxResults) {
        return getEntitiesForUser(entityType, appUser, true, firstResult, maxResults);
    }

    @SuppressWarnings("unchecked")
    private <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, final boolean archivedFlag, int firstResult, int maxResults) {
        Criteria criteria = createCriteriaWithArchiveFlag(entityType, appUser, archivedFlag, firstResult, maxResults);
        return new HashSet<>((List<T>) criteria.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> int getEntitiesForUserCount(final Class<T> entityType, final AppUser appUser) {
        Criteria criteria = createCriteria(entityType, appUser, 0, 0);
        return returnRowCountForCriteria(criteria);
    }

    @Override
    public <T extends AppUserOwnedObject> int getActiveEntitiesForUserCount(final Class<T> entityType, final AppUser appUser) {
        return getEntitiesForUserCount(entityType, appUser, false);
    }

    @Override
    public <T extends AppUserOwnedObject> int getArchivedEntitiesForUserCount(final Class<T> entityType, final AppUser appUser) {
        return getEntitiesForUserCount(entityType, appUser, true);
    }

    @SuppressWarnings("unchecked")
    private <T extends AppUserOwnedObject> int getEntitiesForUserCount(final Class<T> entityType, final AppUser appUser, final boolean archivedFlag) {
        Criteria criteria = createCriteriaWithArchiveFlag(entityType, appUser, archivedFlag, 0, 0);
        return returnRowCountForCriteria(criteria);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> List<String> getModificationsSince(final AppUser appUser, final DateTime since, final int maxResults) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(HibernateHistory.class)
                .add(Restrictions.eq("appUser", appUser))
                .add(Restrictions.gt("modificationTimestampInstant", since.getMillis()))
                .addOrder(Order.asc("modificationTimestampInstant"));
        if (maxResults >= 0) {
            criteria.setMaxResults(maxResults);
        }
        List<HibernateHistory> results = criteria.list();
        Collection<String> transformedResults = Collections2.transform(results, new Function<HibernateHistory, String>() {
            @Nullable
            @Override
            public String apply(@Nullable HibernateHistory input) {
                return input == null ? null : input.getSerializedVersion();
            }
        });

        return new LinkedList<>(transformedResults);
    }

    private Criteria addPhotoForCriteria(final Criteria criteria, final AppUserOwnedObject ownedObject) {
        return criteria.add(Restrictions.eq("photoFor", ownedObject));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createCriteria(HibernatePhoto.class, ownedObject.getAppUser(), firstResult, maxResults), ownedObject);
        return photoFor.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getActivePhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), false, firstResult, maxResults), ownedObject);
        return photoFor.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getArchivedPhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), true, firstResult, maxResults), ownedObject);
        return photoFor.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getAllPhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createCriteria(HibernatePhoto.class, ownedObject.getAppUser(), 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public int getActivePhotosForEntityCount(AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), false, 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public int getArchivedPhotosForEntityCount(AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), true, 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public LocalDateTime getLastObservationTimestampForEntity(final Observable observable) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HibernateObservation.class)
                .add(Restrictions.eq("observationSubject", observable))
                .setProjection(Projections.max("observationTimestamp"));
        LocalDateTime result = (LocalDateTime) criteria.uniqueResult();
        return result == null ? Observable.NEVER_OBSERVED : result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForEntity(final Observable observable) {
        return sessionFactory.getCurrentSession()
                .createCriteria(HibernateObservation.class)
                .add(Restrictions.eq("observationSubject", observable))
                .list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForObservationCategory(final ObservationCategory observationCategory) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HibernateObservation.class);
        if (observationCategory != null) {
            criteria.createCriteria("categories").add(Restrictions.eq("id", observationCategory.getId()));
        } else {
            criteria.add(Restrictions.isEmpty("categories"));
        }
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to) {
        LocalTime time = new LocalTime(0, 0, 0, 0);
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HibernateObservation.class)
                .add(Restrictions.eq("observationSubject", observable))
                .add(Restrictions.ge("observationTimestamp", from.toLocalDateTime(time)))
                .add(Restrictions.lt("observationTimestamp", to.plusDays(1).toLocalDateTime(time)));
        if (observationCategory != null) {
            criteria.createCriteria("categories").add(Restrictions.eq("id", observationCategory.getId()));
        } else {
            criteria.add(Restrictions.isEmpty("categories"));
        }
        return criteria.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Student> getAllStudentsForClassList(final ClassList classList) {
        return sessionFactory.getCurrentSession().createCriteria(HibernateStudent.class)
                .createCriteria("classLists")
                .add(Restrictions.eq("id", classList.getId()))
                .list();
    }

    protected <T extends IdObject> String getHibernateEntityName(final Class<T> entityType) {
        Class<T> wrapperFor;
        if (!HibernateIdObject.class.isAssignableFrom(entityType)) {

            wrapperFor = wrapperFactory.getWrapperForEntity(idObjectReflectionHelper.getIdObjectInterfaceForClass(entityType));
            if (wrapperFor == null) {
                throw new IllegalArgumentException("Unknown entity type " + entityType.getSimpleName());
            }
        } else {
            wrapperFor = entityType;
        }

        return sessionFactory.getClassMetadata(wrapperFor).getEntityName();
    }
}
