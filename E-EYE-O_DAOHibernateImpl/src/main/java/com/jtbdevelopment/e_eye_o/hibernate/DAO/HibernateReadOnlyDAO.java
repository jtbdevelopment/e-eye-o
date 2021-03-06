package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.*;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
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
    protected final IdObjectWrapperFactory wrapperFactory;
    protected final IdObjectFactory idObjectFactory;

    @Autowired
    public HibernateReadOnlyDAO(final SessionFactory sessionFactory, final IdObjectWrapperFactory wrapperFactory, final IdObjectReflectionHelper idObjectReflectionHelper, final IdObjectFactory idObjectFactory) {
        this.sessionFactory = sessionFactory;
        this.wrapperFactory = wrapperFactory;
        this.idObjectReflectionHelper = idObjectReflectionHelper;
        this.idObjectFactory = idObjectFactory;
    }


    @Override
    //  TODO - paging
    @SuppressWarnings("unchecked")
    public Set<AppUser> getUsers() {
        Set<AppUser> returnSet = new LinkedHashSet<>();
        returnSet.addAll(
                (List<AppUser>) sessionFactory.getCurrentSession().
                        createCriteria(HibernateAppUser.class).
                        addOrder(Order.asc("id")).
                        list());
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

    private <T extends AppUserOwnedObject> Criteria createSemesterCriteria(final Semester semester) {
        return sessionFactory.getCurrentSession()
                .createCriteria(getHibernateEntityName(Observation.class))
                .add(Restrictions.eq("appUser", semester.getAppUser()))
                .add(Restrictions.ge("observationTimestamp", semester.getStart().toLocalDateTime(LocalTime.MIDNIGHT)))
                .add(Restrictions.lt("observationTimestamp", semester.getEnd().plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)))
                .addOrder(Order.asc("id"));
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
        AuditReader reader = AuditReaderFactory.get(sessionFactory.getCurrentSession());
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(HibernateAppUserOwnedObject.class, false, true);
        query.add(AuditEntity.property("appUser").eq(appUser))
                .add(
                        AuditEntity.or(
                                AuditEntity.and(
                                        AuditEntity.revisionType().ne(RevisionType.DEL),
                                        AuditEntity.or(
                                                AuditEntity.property("modificationTimestampInstant").gt(since.getMillis()),
                                                AuditEntity.and(
                                                        AuditEntity.property("modificationTimestampInstant").eq(since.getMillis()),
                                                        AuditEntity.id().gt(sinceId)
                                                ))
                                ),
                                AuditEntity.and(
                                        AuditEntity.revisionType().eq(RevisionType.DEL),
                                        AuditEntity.or(
                                                AuditEntity.revisionProperty("timestamp").gt(since.getMillis()),
                                                AuditEntity.and(
                                                        AuditEntity.revisionProperty("timestamp").eq(since.getMillis()),
                                                        AuditEntity.id().gt(sinceId)
                                                ))
                                )
                        )
                )
                .addOrder(AuditEntity.revisionProperty("timestamp").asc())
                .addOrder(AuditEntity.property("modificationTimestampInstant").asc())
                .addOrder(AuditEntity.id().asc());
        if (maxResults >= 0) {
            query.setMaxResults(maxResults);
        }
        List<Object[]> resultSets = query.getResultList();
        List<AppUserOwnedObject> results = new LinkedList<>();
        results.addAll(Collections2.transform(resultSets, new Function<Object[], AppUserOwnedObject>() {
            @Nullable
            @Override
            public AppUserOwnedObject apply(@Nullable final Object[] input) {
                if (input != null) {
                    AppUserOwnedObject owned = (AppUserOwnedObject) input[0];
                    if (RevisionType.DEL.equals(input[2])) {
                        return idObjectFactory.
                                newDeletedObjectBuilder(owned.getAppUser()).
                                withDeletedId(owned.getId()).
                                withId("DELETE" + owned.getId()).
                                withModificationTimestamp(new DateTime(((DefaultRevisionEntity) input[1]).getRevisionDate())).
                                build();
                    } else {
                        return owned;
                    }
                }
                return null;
            }
        }));
        return results;
    }

    private Criteria addPhotoForCriteria(final Criteria criteria, final AppUserOwnedObject ownedObject) {
        return criteria.add(Restrictions.eq("photoFor", ownedObject));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteria(HibernatePhoto.class, ownedObject.getAppUser(), firstResult, maxResults), ownedObject);
        return new HashSet<Photo>(photoFor.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Photo> getActivePhotosForEntity(final AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), false, firstResult, maxResults), ownedObject);
        return new HashSet<Photo>(photoFor.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Photo> getArchivedPhotosForEntity(final AppUserOwnedObject ownedObject, int firstResult, int maxResults) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), true, firstResult, maxResults), ownedObject);
        return new HashSet<Photo>(photoFor.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getAllPhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteria(HibernatePhoto.class, ownedObject.getAppUser(), 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public int getActivePhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), false, 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    public int getArchivedPhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        Criteria photoFor = addPhotoForCriteria(createEntityCriteriaWithArchiveFlag(HibernatePhoto.class, ownedObject.getAppUser(), true, 0, 0), ownedObject);
        return returnRowCountForCriteria(photoFor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getAllObservationsForSemester(final Semester semester) {
        return new HashSet<Observation>(createSemesterCriteria(semester).list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getAllObservationsForEntity(final Observable observable) {
        return new HashSet<Observation>(sessionFactory.getCurrentSession()
                .createCriteria(HibernateObservation.class)
                .add(Restrictions.eq("observationSubject", observable))
                .list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getAllObservationsForObservationCategory(final AppUser user, final ObservationCategory observationCategory) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HibernateObservation.class);
        criteria.add(Restrictions.eq("appUser", user));
        if (observationCategory != null) {
            criteria.createCriteria("categories").add(Restrictions.eq("id", observationCategory.getId()));
        } else {
            criteria.add(Restrictions.isEmpty("categories"));
        }
        return new HashSet<Observation>(criteria.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HibernateObservation.class)
                .add(Restrictions.eq("observationSubject", observable))
                .add(Restrictions.ge("observationTimestamp", from.toLocalDateTime(LocalTime.MIDNIGHT)))
                .add(Restrictions.lt("observationTimestamp", to.plusDays(1).toLocalDateTime(LocalTime.MIDNIGHT)));
        if (observationCategory != null) {
            criteria.createCriteria("categories").add(Restrictions.eq("id", observationCategory.getId()));
        } else {
            criteria.add(Restrictions.isEmpty("categories"));
        }
        return new HashSet<Observation>(criteria.list());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Student> getAllStudentsForClassList(final ClassList classList) {
        return new HashSet<Student>(sessionFactory.getCurrentSession().createCriteria(HibernateStudent.class)
                .createCriteria("classLists")
                .add(Restrictions.eq("id", classList.getId()))
                .list());
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
}
