package com.jtbdevelopment.e_eye_o.mongo.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.mongo.DAO.converters.MongoIdObjectReadConverter;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Date: 1/14/14
 * Time: 12:14 PM
 */
public class MongoReadOnlyDAO implements ReadOnlyDAO {
    @Autowired
    protected Mongo mongoClient;

    @Autowired
    protected MongoIdObjectReadConverter readConverter;

    @Autowired
    protected DBCollection usersCollection;

    @Autowired
    protected DBCollection observablesCollection;

    @Autowired
    protected DBCollection observationsCollection;

    @Autowired
    protected DBCollection photosCollection;

    @Autowired
    protected DBCollection activitiesCollection;

    @Autowired
    protected DBCollection categoriesCollection;

    @Autowired
    protected DBCollection settingsCollection;

    @Autowired
    protected DBCollection ownedCollection;

    @Autowired
    protected DBCollection historyCollection;

    @Override
    public Set<AppUser> getUsers() {
        DBCursor cursor = collectionForEntity(AppUser.class).find();
        cursor.sort(new BasicDBObject("_id", 1));
        HashSet<AppUser> users = new HashSet<>();
        while (cursor.hasNext()) {
            users.add((AppUser) readConverter.convert((BasicDBObject) cursor.next()));
        }
        return users;
    }

    @Override
    public AppUser getUser(final String emailAddress) {
        DBObject result = usersCollection.findOne(new BasicDBObject("AppUser.emailAddress", emailAddress));
        return (AppUser) readConverter.convert((BasicDBObject) result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T get(final Class<T> entityType, final String id) {
        DBCollection collection = collectionForEntity(entityType);
        return (T) readConverter.convert((BasicDBObject) collection.findOne(createIdQuery(id)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> Set<T> getEntitiesForUser(final Class<T> entityType, final AppUser appUser, final int firstResult, final int maxResult) {
        return getPaginatedResults(getDbCursors(entityType, appUser, null), firstResult, maxResult);
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getActiveEntitiesForUser(final Class<T> entityType,
                                                                          final AppUser appUser, final int firstResult, final int maxResult) {
        return getPaginatedResults(getDbCursors(entityType, appUser, Boolean.FALSE), firstResult, maxResult);
    }

    @Override
    public <T extends AppUserOwnedObject> Set<T> getArchivedEntitiesForUser(final Class<T> entityType,
                                                                            final AppUser appUser, final int firstResult, final int maxResult) {
        return getPaginatedResults(getDbCursors(entityType, appUser, Boolean.TRUE), firstResult, maxResult);
    }

    @Override
    public <T extends AppUserOwnedObject> int getEntitiesForUserCount(final Class<T> entityType,
                                                                      final AppUser appUser) {
        return getTotalRowCount(getDbCursors(entityType, appUser, null));
    }

    @Override
    public <T extends AppUserOwnedObject> int getActiveEntitiesForUserCount(final Class<T> entityType, final AppUser appUser) {
        return getTotalRowCount(getDbCursors(entityType, appUser, Boolean.FALSE));
    }

    @Override
    public <T extends AppUserOwnedObject> int getArchivedEntitiesForUserCount(final Class<T> entityType, final AppUser appUser) {
        return getTotalRowCount(getDbCursors(entityType, appUser, Boolean.TRUE));
    }

    @Override
    public Set<Photo> getAllPhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults) {
        return processCursorResults(getPhotos(ownedObject, null), firstResult, maxResults);
    }

    @Override
    public Set<Photo> getActivePhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults) {
        return processCursorResults(getPhotos(ownedObject, false), firstResult, maxResults);
    }

    @Override
    public Set<Photo> getArchivedPhotosForEntity(final AppUserOwnedObject ownedObject, final int firstResult, final int maxResults) {
        return processCursorResults(getPhotos(ownedObject, true), firstResult, maxResults);
    }

    @Override
    public int getAllPhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        return getPhotos(ownedObject, null).count();
    }

    @Override
    public int getActivePhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        return getPhotos(ownedObject, false).count();
    }

    @Override
    public int getArchivedPhotosForEntityCount(final AppUserOwnedObject ownedObject) {
        return getPhotos(ownedObject, true).count();
    }

    @Override
    public Set<Observation> getAllObservationsForSemester(final Semester semester) {
        BasicDBObject q = new BasicDBObject();
        q.put("Observation.appUser._id", new ObjectId(semester.getAppUser().getId()));
        LocalDate from = semester.getStart();
        LocalDate to = semester.getEnd();
        addObservationRangeCondition(q, from, to);
        return processCursorResults(collectionForEntity(Observation.class).find(q), 0, 0);
    }

    @Override
    public Set<Observation> getAllObservationsForEntity(final Observable observable) {
        BasicDBObject q = new BasicDBObject("Observation.observationSubject._id", new ObjectId(observable.getId()));
        return processCursorResults(collectionForEntity(Observation.class).find(q), 0, 0);
    }

    @Override
    public Set<Observation> getAllObservationsForObservationCategory(final AppUser user, final ObservationCategory observationCategory) {
        BasicDBObject q = new BasicDBObject();
        addCategoryCondition(q, observationCategory, user);
        return processCursorResults(collectionForEntity(Observation.class).find(q), 0, 0);
    }

    @Override
    public Set<Observation> getAllObservationsForEntityAndCategory(final Observable observable, final ObservationCategory observationCategory, final LocalDate from, final LocalDate to) {
        BasicDBObject q = new BasicDBObject("Observation.observationSubject._id", new ObjectId(observable.getId()));
        addCategoryCondition(q, observationCategory, observable.getAppUser());
        addObservationRangeCondition(q, from, to);
        return processCursorResults(collectionForEntity(Observation.class).find(q), 0, 0);
    }

    @Override
    public Set<Student> getAllStudentsForClassList(final ClassList classList) {
        BasicDBObject q = new BasicDBObject("Student.classLists._id", new ObjectId(classList.getId()));
        return processCursorResults(collectionForEntity(Student.class).find(q), 0, 0);
    }

    @Override
    public List<? extends AppUserOwnedObject> getModificationsSince(final AppUser appUser, final DateTime since, final String sinceId, final int maxResults) {
        BasicDBObject q = new BasicDBObject("item.appUserId", new ObjectId(appUser.getId()));
        if (StringUtils.hasLength(sinceId)) {
            BasicDBList or = new BasicDBList();
            or.add(new BasicDBObject("item.modificationTimestamp", new BasicDBObject("$gt", since.toDate())));
            BasicDBList andPartOfOr = new BasicDBList();
            andPartOfOr.add(new BasicDBObject("item.modificationTimestamp", since.toDate()));
            andPartOfOr.add(new BasicDBObject("item._id", new BasicDBObject("$gt", new ObjectId(sinceId))));
            or.add(andPartOfOr);
            q.put("$or", or);
        } else {
            q.put("item.modificationTimestamp", new BasicDBObject("$gt", since.toDate()));
        }


        DBCursor cursor = historyCollection.find(q);
        BasicDBObject sort = new BasicDBObject();
        sort.put("item.modificationTimestamp", 1);
        sort.put("item._id", 1);
        cursor.sort(sort);
        if (maxResults > 0) {
            cursor.limit(maxResults);
        }

        List<AppUserOwnedObject> events = new LinkedList<>();
        while (cursor.hasNext()) {
            events.add((AppUserOwnedObject) readConverter.convert((BasicDBObject) cursor.next().get("item")));
        }
        return events;
    }

    protected void addCategoryCondition(final BasicDBObject q, final ObservationCategory observationCategory, final AppUser user) {
        if (observationCategory == null) {
            q.put("Observation.categories", new BasicDBObject("$size", 0));
            q.put("Observation.appUser._id", new ObjectId(user.getId()));
        } else {
            q.put("Observation.categories._id", new ObjectId(observationCategory.getId()));
        }
    }

    protected void addObservationRangeCondition(final BasicDBObject q, final LocalDate from, final LocalDate to) {
        BasicDBList range = new BasicDBList();
        range.add(new BasicDBObject("Observation.observationTimestamp", new BasicDBObject("$gte", from.toDate())));
        range.add(new BasicDBObject("Observation.observationTimestamp", new BasicDBObject("$lt", to.plusDays(1).toDate())));
        q.put("$and", range);
    }

    protected <T extends AppUserOwnedObject> Set<T> getPaginatedResults(final List<DBCursor> cursors, final int firstResult, final int maxResult) {
        Set<T> results = new HashSet<>();
        int remainingSkip = firstResult;
        int remainingMax = maxResult;
        for (DBCursor cursor : cursors) {
            int cursorCount = cursor.count();
            if (remainingSkip > 0) {
                if (cursorCount > remainingSkip) {
                    cursor.skip(remainingSkip);
                    remainingSkip = 0;
                } else {
                    remainingSkip -= cursorCount;
                    continue;
                }
            }

            if (remainingMax > 0) {
                cursor.limit(remainingMax);
            }
            int preCounter = results.size();
            while (cursor.hasNext()) {
                results.add((T) readConverter.convert((BasicDBObject) cursor.next()));
            }
            remainingMax -= (results.size() - preCounter);
            if (remainingMax < 1 && maxResult > 0) {
                return results;
            }
        }
        return results;
    }

    protected int getTotalRowCount(final List<DBCursor> cursors) {
        int count = 0;
        for (DBCursor cursor : cursors) {
            count += cursor.count();
        }
        return count;
    }

    protected <T extends AppUserOwnedObject> List<DBCursor> getDbCursors(final Class<T> entityType,
                                                                         final AppUser appUser, final Boolean archived) {
        List<DBCursor> cursors = new LinkedList<>();
        boolean ownedGeneric = AppUserOwnedObject.class.getSimpleName().equals(entityType.getSimpleName());
        boolean observableGeneric = Observable.class.getSimpleName().equals(entityType.getSimpleName());
        for (Class<? extends AppUserOwnedObject> loop : Arrays.asList(Semester.class, ClassList.class, Student.class, Observation.class, Photo.class, TwoPhaseActivity.class, AppUserSettings.class, ObservationCategory.class)) {
            if (ownedGeneric ||
                    loop.isAssignableFrom(entityType) ||
                    (Observable.class.isAssignableFrom(loop) && observableGeneric && !ownedGeneric)) {
                BasicDBObject dbObject = new BasicDBObject(loop.getSimpleName() + ".appUser._id", new ObjectId(appUser.getId()));
                if (archived != null) {
                    dbObject.put(loop.getSimpleName() + ".archived", archived);
                }
                DBCursor cursor = collectionForEntity(loop).find(dbObject);
                cursor.sort(new BasicDBObject("_id", 1));
                cursors.add(cursor);
            }
        }
        return cursors;
    }

    private <T extends AppUserOwnedObject> Set<T> processCursorResults(final DBCursor cursor, final int firstResult, final int maxResults) {
        if (firstResult > 0) {
            cursor.skip(firstResult);
        }
        if (maxResults > 0) {
            cursor.limit(maxResults);
        }
        Set<T> items = new HashSet<>();
        while (cursor.hasNext()) {
            items.add((T) readConverter.convert((BasicDBObject) cursor.next()));
        }
        return items;
    }

    protected DBCursor getPhotos(final AppUserOwnedObject ownedObject, final Boolean archived) {
        BasicDBObject q = new BasicDBObject("Photo.photoFor._id", new ObjectId(ownedObject.getId()));
        if (archived != null) {
            q.put("Photo.archived", archived);
        }
        return photosCollection.find(q);
    }

    protected DBCollection collectionForEntity(final Class<? extends IdObject> entityType) {
        if (AppUser.class.isAssignableFrom(entityType)) {
            return usersCollection;
        }
        if (Observable.class.isAssignableFrom(entityType)) {
            return observablesCollection;
        }
        if (Observation.class.isAssignableFrom(entityType)) {
            return observationsCollection;
        }
        if (Photo.class.isAssignableFrom(entityType)) {
            return photosCollection;
        }
        if (Semester.class.isAssignableFrom(entityType)) {
            return ownedCollection;
        }
        if (TwoPhaseActivity.class.isAssignableFrom(entityType)) {
            return activitiesCollection;
        }
        if (AppUserSettings.class.isAssignableFrom(entityType)) {
            return settingsCollection;
        }
        if (ObservationCategory.class.isAssignableFrom(entityType)) {
            return categoriesCollection;
        }
        throw new RuntimeException("Unknown entityType " + entityType.getCanonicalName());
    }

    protected BasicDBObject createIdQuery(final String id) {
        return new BasicDBObject("_id", new ObjectId(id));
    }

}
