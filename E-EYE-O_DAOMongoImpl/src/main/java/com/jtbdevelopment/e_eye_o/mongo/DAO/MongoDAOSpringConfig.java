package com.jtbdevelopment.e_eye_o.mongo.DAO;

import com.mongodb.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Properties;

/**
 * Date: 1/13/14
 * Time: 8:36 PM
 */
@Configuration
@ImportResource(value = "classpath*:spring-context-mongo.xml")
public class MongoDAOSpringConfig {

    private static BasicDBObject UNIQUE = new BasicDBObject("unique", Boolean.TRUE);
    private static BasicDBObject SPARSE = new BasicDBObject("sparse", Boolean.TRUE);
    private static DBObject UNIQUESPARSE = new BasicDBObject() {{
        put("unique", Boolean.TRUE);
        put("sparse", Boolean.TRUE);
    }};

    @Bean
    public Mongo mongo(final @Qualifier("mongoProperties") Properties mongoProperties,
                       final @Qualifier("mongoOverrideProperties") Properties mongoOverrideProperties) throws Exception {
        String connectionPropName = "connection";
        String defaultConnection = "localhost";
        //  TODO - user
        return new MongoClient(
                mongoOverrideProperties.getProperty(connectionPropName,
                        mongoProperties.getProperty(connectionPropName, defaultConnection)));
    }

    @Bean
    @Autowired
    public DB mongoDatabase(final Mongo mongo, final @Qualifier("mongoProperties") Properties mongoProperties,
                            final @Qualifier("mongoOverrideProperties") Properties mongoOverrideProperties) {
        String dbNameProp = "databaseName";
        String defaultDbName = "eeyeodb";
        String recreatePropName = "recreateDatabase";
        String defaultRecreate = "false";
        String databaseName = mongoOverrideProperties.getProperty(dbNameProp, mongoProperties.getProperty(dbNameProp, defaultDbName));
        if (Boolean.parseBoolean(
                mongoOverrideProperties.getProperty(recreatePropName,
                        mongoProperties.getProperty(recreatePropName, defaultRecreate)))
                ) {
            try {
                mongo.dropDatabase(databaseName);
            } catch (Exception e) {
                //
            }
        }
        mongo.setWriteConcern(WriteConcern.JOURNALED);
        return mongo.getDB(databaseName);
    }

    @Bean
    @Autowired
    public DBCollection usersCollection(final DB db) {
        DBCollection users = db.getCollection("users");
        BasicDBObject index;
        index = new BasicDBObject("AppUser.emailAddress", 1);
        users.ensureIndex(index, UNIQUE);
        return users;
    }

    @Bean
    @Autowired
    public DBCollection ownedCollection(final DB db) {
        DBCollection objects = db.getCollection("owned");

        DBObject index = new BasicDBObject("appUserId", 1);
        index.put("archived", 1);
        objects.ensureIndex(index);

        index = new BasicDBObject("Semester.appUser._id", 1);
        index.put("Semester.archived", 1);
        objects.ensureIndex(index, SPARSE);

        index = new BasicDBObject("TwoPhaseActivity.appUser._id", 1);
        index.put("TwoPhaseActivity.archived", 1);
        objects.ensureIndex(index, SPARSE);

        index = new BasicDBObject("ObservationCategory.appUser._id", 1);
        index.put("ObservationCategory.shortName", 1);
        objects.ensureIndex(index, UNIQUESPARSE);
        index = new BasicDBObject("ObservationCategory.appUser._id", 1);
        index.put("ObservationCategory.archived", 1);
        objects.ensureIndex(index, SPARSE);

        index = new BasicDBObject("AppUserSettings.appUser._id", 1);
        objects.ensureIndex(index, UNIQUESPARSE);

        index = new BasicDBObject("Photo.appUser._id", 1);
        index.put("Photo.archived", 1);
        objects.ensureIndex(index, SPARSE);
        index = new BasicDBObject("Photo.photoFor._id", 1);
        index.put("Photo.archived", 1);
        objects.ensureIndex(index, SPARSE);

        index = new BasicDBObject("ClassList.appUser._id", 1);
        index.put("ClassList.archived", 1);
        objects.ensureIndex(index, SPARSE);
        index = new BasicDBObject("Student.appUser._id", 1);
        index.put("Student.archived", 1);
        objects.ensureIndex(index, SPARSE);
        index = new BasicDBObject("Student.classLists._id", 1);
        index.put("_id", 1);
        objects.ensureIndex(index, UNIQUESPARSE);


        index = new BasicDBObject("Observation.appUser._id", 1);
        index.put("Observation.categories._id", 1);
        index.put("_id", 1);
        objects.ensureIndex(index, UNIQUESPARSE);
        index = new BasicDBObject("Observation.observationSubject._id", 1);
        index.put("Observation.observationTimestamp", 1);
        index.put("Observation.categories._id", 1);
        objects.ensureIndex(index, SPARSE);
        index = new BasicDBObject("Observation.categories._id", 1);
        objects.ensureIndex(index, SPARSE);
        index = new BasicDBObject("Observation.appUser._id", 1);
        index.put("Observation.observationTimestamp", 1);
        objects.ensureIndex(index, SPARSE);
        index = new BasicDBObject("Observation.appUser._id", 1);
        index.put("Observation.archived", 1);
        objects.ensureIndex(index, SPARSE);

        return objects;
    }

    @Bean
    @Autowired
    public DBCollection observationsCollection(final DB db) {
        DBCollection observations = db.getCollection("observations");
        return observations;
    }

    @Bean
    @Autowired
    public DBCollection photosCollection(final DB db) {
        DBCollection photos = db.getCollection("photos");
        return photos;
    }

    @Bean
    @Autowired
    public DBCollection cookiesCollection(final DB db) {
        DBCollection cookies = db.getCollection("cookies");
        DBObject index = new BasicDBObject("userName", 1);
        cookies.ensureIndex(index);
        index = new BasicDBObject("series", 1);
        cookies.ensureIndex(index, UNIQUE);
        index = new BasicDBObject("date", 1);
        cookies.ensureIndex(index);
        return cookies;
    }

    @Bean
    @Autowired
    public DBCollection historyCollection(final DB db) {
        DBCollection history = db.getCollection("history");
        DBObject index = new BasicDBObject();
        index.put("item.appUserId", 1);
        index.put("item.modificationTimestamp", 1);
        index.put("_id", 1);
        history.ensureIndex(index);
        return history;
    }

    @Bean
    @Autowired
    public ValidatorFactory validationFactory() {
        return Validation.buildDefaultValidatorFactory();

    }

    @Bean
    @Autowired
    public Validator validator(final ValidatorFactory validatorFactory) {
        return validatorFactory.getValidator();
    }
}
