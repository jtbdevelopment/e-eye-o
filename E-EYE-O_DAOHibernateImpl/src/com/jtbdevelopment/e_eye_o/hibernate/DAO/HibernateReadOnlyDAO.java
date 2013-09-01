package com.jtbdevelopment.e_eye_o.hibernate.DAO;


import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.entities.wrapper.DAOIdObjectWrapperFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.impl.HibernateIdObject;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
        returnSet.addAll((List<AppUser>) sessionFactory.getCurrentSession().createQuery("from AppUser").list());
        return returnSet;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AppUser getUser(final String emailAddress) {
        final Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where emailAddress = :emailAddress");
        query.setParameter("emailAddress", emailAddress);
        List<AppUser> users = (List<AppUser>) query.list();
        if (users.isEmpty()) {
            return null;
        }
        if (users.size() > 1) {
            throw new IllegalStateException("Duplicate users with same emailAddress - not possible");
        }
        return users.get(0);
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
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser) {
        Query query = sessionFactory.getCurrentSession().createQuery("from " + getHibernateEntityName(entityType) + " where appUser = :user");
        query.setParameter("user", appUser);
        return new HashSet<>((List<T>) query.list());
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(final Class<T> entityType, final AppUser appUser) {
        return getEntitiesForUser(entityType, appUser, false);
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType, final AppUser appUser) {
        return getEntitiesForUser(entityType, appUser, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> List<String> getModificationsSince(final AppUser appUser, final DateTime since) {
        Query query = sessionFactory.getCurrentSession().createQuery("from HistoricalFeed where appUser = :user and modificationTimestamp > :since");
        query.setParameter("user", appUser);
        query.setParameter("since", since.getMillis());   //  See HibernateIdObject getModificationTimestamp
        List<HibernateHistory> results = query.list();
        List<HibernateHistory> sortedResults = new LinkedList<>(results);
        Collections.sort(sortedResults, new Comparator<HibernateHistory>() {
            @Override
            public int compare(final HibernateHistory o1, final HibernateHistory o2) {
                int i = o1.getModificationTimestamp().compareTo(o2.getModificationTimestamp());
                if (i == 0) {
                    return o1.getId().compareTo(o2.getId());
                }
                return i;
            }
        });
        Collection<String> transformedResults = Collections2.transform(sortedResults, new Function<HibernateHistory, String>() {
            @Nullable
            @Override
            public String apply(@Nullable HibernateHistory input) {
                return input.getSerializedVersion();
            }
        });

        return new LinkedList<>(transformedResults);
    }

    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, final boolean archivedFlag) {
        return new HashSet<>(Collections2.filter(getEntitiesForUser(entityType, appUser), new Predicate<T>() {
            @Override
            public boolean apply(@Nullable final T input) {
                return (input != null) && (input.isArchived() == archivedFlag);
            }
        }));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Photo where photoFor = :photoFor");
        query.setParameter("photoFor", ownedObject);
        return (List<Photo>) query.list();
    }

    @Override
    public LocalDateTime getLastObservationTimestampForEntity(final Observable observable) {
        Query query = sessionFactory.getCurrentSession().createQuery("select max(observationTimestamp) from Observation where observationSubject = :observationSubject");
        query.setParameter("observationSubject", observable);
        LocalDateTime result = (LocalDateTime) query.uniqueResult();
        return result == null ? Observable.NEVER_OBSERVED : result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForEntity(final Observable observable) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Observation where observationSubject = :observationSubject");
        query.setParameter("observationSubject", observable);
        return (List<Observation>) query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Observation> getAllObservationsForObservationCategory(final ObservationCategory observationCategory) {
        Query query;
        if (observationCategory != null) {
            query = sessionFactory.getCurrentSession().createQuery("from Observation as O where :category member of O.categories");
            query.setParameter("category", observationCategory);
        } else {
            query = sessionFactory.getCurrentSession().createQuery("from Observation as O where size( O.categories ) = 0");
        }
        return (List<Observation>) query.list();
    }

    @Override
    public List<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to) {
        LocalDateTime adjustedTo = new LocalDateTime(to.plusDays(1));
        Query query;
        if (observationCategory != null) {
            query = sessionFactory.getCurrentSession().createQuery("from Observation as O where observationSubject = :observationSubject AND :category member of O.categories AND observationTimestamp >= :from and observationTimestamp < :to");
            query.setParameter("category", observationCategory);
        } else {
            query = sessionFactory.getCurrentSession().createQuery("from Observation as O where observationSubject = :observationSubject AND size( O.categories ) = 0 AND observationTimestamp >= :from and observationTimestamp < :to");
        }
        query.setParameter("observationSubject", observable);
        query.setParameter("from", new LocalDateTime(from));
        query.setParameter("to", adjustedTo);

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
