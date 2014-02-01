package com.jtbdevelopment.e_eye_o.mongo.DAO;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.FieldUpdateValidator;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.events.IdObjectChangedPublisher;
import com.jtbdevelopment.e_eye_o.mongo.DAO.converters.MongoIdObjectWriteConverter;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.*;

/**
 * Date: 1/14/14
 * Time: 12:15 PM
 */
@Repository
public class MongoReadWriteDAO extends MongoReadOnlyDAO implements ReadWriteDAO {

    @Autowired
    protected Validator validator;

    @Autowired
    protected MongoIdObjectWriteConverter writeConverter;
    @Autowired
    protected IdObjectChangedPublisher idObjectChangedPublisher;
    @Autowired
    protected IdObjectReflectionHelper idObjectReflectionHelper;
    @Autowired
    private FieldUpdateValidator fieldUpdateValidator;
    @Autowired
    protected IdObjectFactory idObjectFactory;


    private <T extends IdObject> void validate(final T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public <T extends IdObject> T create(final T entity) {
        if (entity instanceof DeletedObject) {
            throw new IllegalArgumentException("You cannot explicitly create a DeletedObject.");
        }
        entity.setId(new ObjectId().toString());
        entity.setModificationTimestamp(DateTime.now());
        validate(entity);
        DBObject convert = writeConverter.convert(entity);
        WriteResult result = collectionForEntity(entity.getClass()).insert(convert);
        if (result.getLastError().get("err") != null) {

        }
        IdObject created = (T) readConverter.convert((BasicDBObject) convert);
        if (entity instanceof AppUserOwnedObject && !(entity instanceof AppUserSettings)) {
            historyCollection.insert(new BasicDBObject("item", convert));
        }
        dealWithObservationChanges(created);
        idObjectChangedPublisher.publishCreate(created);
        return (T) created;
    }

    @Override
    public <T extends IdObject> T update(final AppUser updatingAppUser, final T entity) {
        if (!idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass()).getAnnotation(IdObjectEntitySettings.class).editable()) {
            throw new IllegalArgumentException("You cannot explicitly update a " + entity.getClass() + ".");
        }
        @SuppressWarnings("unchecked")
        final T existing = get((Class<T>) entity.getClass(), entity.getId());
        fieldUpdateValidator.removeInvalidFieldUpdates(updatingAppUser, existing, entity);
        T saved = trustedUpdate(entity);
        dealWithObservationChanges(saved);
        return saved;
    }

    @Override
    public <T extends IdObject> T trustedUpdate(final T entity) {
        entity.setModificationTimestamp(DateTime.now());
        validate(entity);
        DBObject convert = writeConverter.convert(entity);
        WriteResult result;
        result = collectionForEntity(entity.getClass()).update(createIdQuery(entity.getId()), convert);
        if (entity instanceof AppUserOwnedObject && !(entity instanceof AppUserSettings)) {
            historyCollection.insert(new BasicDBObject("item", convert));
        }
        updateCopies(entity, convert);
        if (result.getLastError().get("err") != null) {
        }
        IdObject created = (T) readConverter.convert((BasicDBObject) convert);
        idObjectChangedPublisher.publishUpdate(created);
        return (T) created;
    }

    /*
     *  Update AppUser
     */
    private final static Map<Class<? extends IdObject>, List<Class<? extends AppUserOwnedObject>>> contains = new HashMap<Class<? extends IdObject>, List<Class<? extends AppUserOwnedObject>>>() {
        {
            put(AppUser.class, Arrays.asList(Observable.class, TwoPhaseActivity.class, Semester.class, Observation.class, AppUserSettings.class, Photo.class, ObservationCategory.class));
            put(Observation.class, Arrays.<Class<? extends AppUserOwnedObject>>asList(Photo.class));
            put(ObservationCategory.class, Arrays.<Class<? extends AppUserOwnedObject>>asList(Observation.class));
            put(ClassList.class, Arrays.<Class<? extends AppUserOwnedObject>>asList(Observation.class, Photo.class, Student.class));
            put(Student.class, Arrays.<Class<? extends AppUserOwnedObject>>asList(Observation.class, Photo.class));
        }
    };

    protected <T extends IdObject> void updateCopies(final T entity, final DBObject convert) {
        //  TODO - this - better
        Class iFace = idObjectReflectionHelper.getIdObjectInterfaceForClass(entity.getClass());
        if (contains.containsKey(iFace)) {
            for (Class<? extends AppUserOwnedObject> updates : contains.get(iFace)) {
                if (entity instanceof AppUser) {
                    collectionForEntity(updates).update(
                            new BasicDBObject("appUser._id", new ObjectId(entity.getId())),
                            new BasicDBObject("$set", new BasicDBObject("appUser", convert)),
                            false, true
                    );
                }
                if ((Photo.class.isAssignableFrom(updates)) && (entity instanceof AppUserOwnedObject)) {
                    collectionForEntity(updates).update(
                            new BasicDBObject("photoFor._id", new ObjectId(entity.getId())),
                            new BasicDBObject("$set", new BasicDBObject("photoFor", convert)),
                            false, true
                    );
                }
                if ((Observation.class.isAssignableFrom(updates)) && (entity instanceof Observable)) {
                    collectionForEntity(updates).update(
                            new BasicDBObject("observationSubject._id", new ObjectId(entity.getId())),
                            new BasicDBObject("$set", new BasicDBObject("observationSubject", convert)),
                            false, true
                    );
                }
                if ((Student.class.isAssignableFrom(updates)) && (entity instanceof ClassList)) {
                    collectionForEntity(updates).update(
                            new BasicDBObject("classList._id", new ObjectId(entity.getId())),
                            new BasicDBObject("$set", new BasicDBObject("classList.$", convert)),
                            false, true
                    );
                }
                if ((Observation.class.isAssignableFrom(updates)) && (entity instanceof ObservationCategory)) {
                    collectionForEntity(updates).update(
                            new BasicDBObject("categories._id", new ObjectId(entity.getId())),
                            new BasicDBObject("$set", new BasicDBObject("categories.$", convert)),
                            false, true
                    );
                }
            }
        }
    }

    @Override
    public <T extends IdObject> List<T> trustedUpdates(final Collection<T> entities) {
        List<T> results = new ArrayList<>(entities.size());
        for (T entity : entities) {
            results.add(trustedUpdate(entity));
        }
        return results;
    }

    @Override
    public <T extends IdObject> void trustedDelete(final T entity) {
        collectionForEntity(entity.getClass()).remove(new BasicDBObject("_id", new ObjectId(entity.getId())));
        if (entity instanceof AppUserOwnedObject && !(entity instanceof AppUserSettings)) {
            historyCollection.insert(new BasicDBObject("item",
                    writeConverter.convert(
                            idObjectFactory.newDeletedObjectBuilder(((AppUserOwnedObject) entity).getAppUser())
                                    .withDeletedId(entity.getId())
                                    .build()
                    )));
        }
        dealWithObservationChanges(entity);
        idObjectChangedPublisher.publishDelete(entity);
    }

    @Override
    public AppUser updateAppUserLogout(final AppUser appUser) {
        return (AppUser) readConverter.convert(
                (BasicDBObject) collectionForEntity(AppUser.class).findAndModify(
                        new BasicDBObject("_id", new ObjectId(appUser.getId())),
                        new BasicDBObject("$set", new BasicDBObject("lastLogout", DateTime.now().toDate()))));
    }

    private <T extends IdObject> void dealWithObservationChanges(final T entity) {
        if (entity instanceof Observation) {
            Observable observed = get(Observable.class, ((Observation) entity).getObservationSubject().getId());
            BasicDBObject result = (BasicDBObject) collectionForEntity(Observation.class).findOne(
                    new BasicDBObject("observationSubject._id", new ObjectId(observed.getId())),
                    new BasicDBObject("observationTimestamp", 1),
                    new BasicDBObject("observationTimestamp", 0)
            );
            LocalDateTime lastObservation;
            if (result == null) {
                lastObservation = Observable.NEVER_OBSERVED;
            } else {
                lastObservation = new LocalDateTime(result.getDate("observationTimestamp"));
            }
            if (observed.getLastObservationTimestamp().compareTo(lastObservation) != 0) {
                observed.setLastObservationTimestamp(lastObservation);
                trustedUpdate(observed);
            }
        }
    }
}
