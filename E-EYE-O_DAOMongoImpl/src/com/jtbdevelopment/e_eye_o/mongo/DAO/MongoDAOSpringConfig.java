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
@ImportResource(value = "classpath*:spring-context*.xml")
public class MongoDAOSpringConfig {

    private static BasicDBObject UNIQUE = new BasicDBObject("unique", Boolean.TRUE);

    @Bean
    public Mongo mongo(final @Qualifier("mongoProperties") Properties mongoProperties,
                       final @Qualifier("mongoOverrideProperties") Properties mongoOverrideProperties) throws Exception {
        String connectionPropName = "connection";
        String defaultConnection = "localhost";
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
        index = new BasicDBObject("emailAddress", 1);
        users.ensureIndex(index, UNIQUE);
        return users;
    }

    @Bean
    @Autowired
    public DBCollection settingsCollection(final DB db) {
        DBCollection settings = db.getCollection("settings");
        BasicDBObject index = new BasicDBObject("appUser._id", 1);
        settings.ensureIndex(index, UNIQUE);
        return settings;
    }

    @Bean
    @Autowired
    public DBCollection categoriesCollection(final DB db) {
        DBCollection categories = db.getCollection("categories");
        BasicDBObject index = new BasicDBObject("appUser._id", 1);
        index.put("shortName", 1);
        categories.ensureIndex(index, UNIQUE);
        return categories;
    }

    @Bean
    @Autowired
    public DBCollection observablesCollection(final DB db) {
        DBCollection observables = db.getCollection("observables");
        DBObject index = new BasicDBObject("appUser._id", 1);
        observables.ensureIndex(index);
        index = new BasicDBObject("classLists._id", 1);
        index = new BasicDBObject("_id", 1);
        observables.ensureIndex(index, UNIQUE);
        return observables;
    }

    @Bean
    @Autowired
    public DBCollection observationsCollection(final DB db) {
        DBCollection observations = db.getCollection("observations");
        DBObject index = new BasicDBObject("appUser._id", 1);
        index.put("categories._id", 1);
        index.put("_id", 1);
        observations.ensureIndex(index, UNIQUE);
        index = new BasicDBObject("observationSubject._id", 1);
        index.put("observationTimestamp", 1);
        index.put("categories._id", 1);
        observations.ensureIndex(index);
        index = new BasicDBObject("categories._id", 1);
        observations.ensureIndex(index);
        index = new BasicDBObject("observationTimestamp", 1);
        observations.ensureIndex(index);
        return observations;
    }

    @Bean
    @Autowired
    public DBCollection semestersCollection(final DB db) {
        DBCollection semesters = db.getCollection("semesters");
        DBObject index = new BasicDBObject("appUser._id", 1);
        semesters.ensureIndex(index);
        return semesters;
    }

    @Bean
    @Autowired
    public DBCollection photosCollection(final DB db) {
        DBCollection photos = db.getCollection("photos");
        DBObject index = new BasicDBObject("appUser._id", 1);
        photos.ensureIndex(index);
        index = new BasicDBObject("photoFor._id", 1);
        photos.ensureIndex(index);
        return photos;
    }

    @Bean
    @Autowired
    public DBCollection activitiesCollection(final DB db) {
        DBCollection activities = db.getCollection("activities");
        DBObject index = new BasicDBObject("appUser._id", 1);
        activities.ensureIndex(index);
        return activities;
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
        index.put("item.appUser._id", 1);
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
