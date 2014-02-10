package com.jtbdevelopment.e_eye_o.mongo.DAO

import com.jtbdevelopment.e_eye_o.DAO.AbstractUserMaintenanceIntegration
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.mongo.DAO.converters.MongoIdObjectWriteConverter
import com.mongodb.DBCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration

/**
 * Date: 1/5/14
 * Time: 3:05 PM
 */
@ContextConfiguration("/test-integration-context.xml")
class MongoUserMaintenanceIntegration extends AbstractUserMaintenanceIntegration {
    @Autowired
    private DBCollection ownedCollection;

    @Autowired
    private MongoIdObjectWriteConverter writeConverter;

    @Override
    void saveDirectly(final IdObject entity) {
        ownedCollection.save(writeConverter.convert(entity));
    }
}
