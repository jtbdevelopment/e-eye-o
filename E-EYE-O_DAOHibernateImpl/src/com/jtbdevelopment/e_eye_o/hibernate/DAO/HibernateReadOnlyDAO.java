package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.*;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectDeserializer;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class HibernateReadOnlyDAO implements ReadOnlyDAO, ApplicationContextAware {
    private final static Logger logger = LoggerFactory.getLogger(HibernateReadOnlyDAO.class);

    protected final IdObjectReflectionHelper idObjectReflectionHelper;
    protected final SessionFactory sessionFactory;
    protected final IdObjectWrapperFactory wrapperFactory;
    private JSONIdObjectDeserializer jsonIdObjectDeserializer;

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Autowired
    public HibernateReadOnlyDAO(final SessionFactory sessionFactory, final IdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper) {
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

    private <T extends AppUserOwnedObject> Criteria createEntityCriteria(Class<T> entityType, AppUser appUser, int firstResult, int maxResults) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(getHibernateEntityName(entityType))
                .add(Restrictions.eq("appUser", appUser))
                .addOrder(Order.asc("id"));
        addPaginationToCriteria(firstResult, maxResults, criteria);
        return criteria;
    }

    private void addPaginationToCriteria(int firstResult, int maxResults, Criteria criteria) {
        if (maxResults > 0) {
            criteria.setFirstResult(firstResult);
            criteria.setMaxResults(maxResults);
        }
    }

    private <T extends AppUserOwnedObject> Criteria createEntityCriteriaWithArchiveFlag(Class<T> entityType, AppUser appUser, boolean archivedFlag, int firstResult, int maxResults) {
        Criteria criteria = createEntityCriteria(entityType, appUser, firstResult, maxResults);
        addArchiveCriteriaToCriteria(archivedFlag, criteria);
        return criteria;
    }

    private void addArchiveCriteriaToCriteria(boolean archivedFlag, Criteria criteria) {
        criteria.add(Restrictions.eq("archived", archivedFlag));
    }

    private <T extends AppUserOwnedObject> Criteria createSemesterCritera(final Semester semester, int firstResult, int maxResults) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(getHibernateEntityName(Observation.class))
                .add(Restrictions.eq("appUser", semester.getAppUser()))
                .add(Restrictions.ge("observationTimestamp", semester.getStart().toLocalDateTime(LocalTime.MIDNIGHT)))
                .add(Restrictions.lt("observationTimestamp", semester.getEnd().plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)))
                .addOrder(Order.asc("id"));
        addPaginationToCriteria(firstResult, maxResults, criteria);
        return criteria;
    }

    private <T extends AppUserOwnedObject> Criteria createSemesterCriteraWithArchiveFlag(final Semester semester, boolean archivedFlag, int firstResult, int maxResults) {
        Criteria criteria = createSemesterCritera(semester, firstResult, maxResults);
        addArchiveCriteriaToCriteria(archivedFlag, criteria);
        return criteria;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, int firstResult, int maxResults) {
        Criteria criteria = createEntityCriteria(entityType, appUser, firstResult, maxResults);
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
        Criteria criteria = createEntityCriteriaWithArchiveFlag(entityType, appUser, archivedFlag, firstResult, maxResults);
        return new HashSet<>((List<T>) criteria.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> int getEntitiesForUserCount(final Class<T> entityType, final AppUser appUser) {
        Criteria criteria = createEntityCriteria(entityType, appUser, 0, 0);
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
        Criteria criteria = createEntityCriteriaWithArchiveFlag(entityType, appUser, archivedFlag, 0, 0);
        return returnRowCountForCriteria(criteria);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<? extends AppUserOwnedObject> getModificationsSince(final AppUser appUser, final DateTime since, final String sinceId, final int maxResults) {
        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(HibernateHistory.class)
                .add(Restrictions.eq("appUser", appUser))
                .add(Restrictions.or(
                        Restrictions.gt("modificationTimestampInstant", since.getMillis()),
                        Restrictions.and(
                                Restrictions.eq("modificationTimestampInstant", since.getMillis()),
                                Restrictions.gt("id", sinceId)
                        ))
                )
                .addOrder(Order.asc("modificationTimestampInstant"))
                .addOrder(Order.asc("id"));
        if (maxResults >= 0) {
            criteria.setMaxResults(maxResults);
        }
        List<HibernateHistory> results = criteria.list();
        final JSONIdObjectDeserializer jsonIdObjectDeserializer = getJSONIdObjectDeserializer();
        Collection<? extends AppUserOwnedObject> transformedResults = Collections2.transform(results, new Function<HibernateHistory, AppUserOwnedObject>() {
            @Nullable
            @Override
            public AppUserOwnedObject apply(@Nullable HibernateHistory input) {
                return (AppUserOwnedObject) (input == null ? null : jsonIdObjectDeserializer.readAsObjects(input.getSerializedVersion()));
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
        Criteria photoFor = addPhotoForCriteria(createEntityCriteria(HibernatePhoto.class, ownedObject.getAppUser(), firstResult, maxResults), ownedObject);
        return photoFor.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getActivePhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), false, firstResult, maxResults), ownedObject);
        return photoFor.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getArchivedPhotosForEntity(AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), true, firstResult, maxResults), ownedObject);
        return photoFor.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getAllPhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteria(HibernatePhoto.class, ownedObject.getAppUser(), 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public int getActivePhotosForEntityCount(AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), false, 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public int getArchivedPhotosForEntityCount(AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), true, 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getAllObservationsForSemester(final Semester semester, final int firstResult, final int maxResults) {
        return new HashSet<Observation>(createSemesterCritera(semester, firstResult, maxResults).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getActiveObservationsForSemester(final Semester semester, final int firstResult, final int maxResults) {
        return new HashSet<Observation>(createSemesterCriteraWithArchiveFlag(semester, false, firstResult, maxResults).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getArchivedObservationsForSemester(final Semester semester, final int firstResult, final int maxResults) {
        return new HashSet<Observation>(createSemesterCriteraWithArchiveFlag(semester, true, firstResult, maxResults).list());
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
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HibernateObservation.class)
                .add(Restrictions.eq("observationSubject", observable))
                .add(Restrictions.ge("observationTimestamp", from.toLocalDateTime(LocalTime.MIDNIGHT)))
                .add(Restrictions.lt("observationTimestamp", to.plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)));
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

            wrapperFor = wrapperFactory.getWrapperForEntity(IdObjectWrapperFactory.WrapperKind.DAO, idObjectReflectionHelper.getIdObjectInterfaceForClass(entityType));
            if (wrapperFor == null) {
                throw new IllegalArgumentException("Unknown entity type " + entityType.getSimpleName());
            }
        } else {
            wrapperFor = entityType;
        }

        return sessionFactory.getClassMetadata(wrapperFor).getEntityName();
    }

    protected JSONIdObjectDeserializer getJSONIdObjectDeserializer() {
        if (jsonIdObjectDeserializer == null) {
            jsonIdObjectDeserializer = applicationContext.getBean(JSONIdObjectDeserializer.class);
        }
        return jsonIdObjectDeserializer;
    }
}
