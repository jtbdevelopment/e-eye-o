package com.jtbdevelopment.e_eye_o.mongo.security;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Component;

/**
 * Date: 1/26/14
 * Time: 11:30 AM
 */
@Component
public class MongoPersistentRememberMeTokenWriteConverter implements Converter<PersistentRememberMeToken, DBObject> {
    @Override
    public DBObject convert(final PersistentRememberMeToken source) {
        if (source == null) {
            return null;
        }
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("series", source.getSeries());
        dbObject.put("date", source.getDate());
        dbObject.put("tokenValue", source.getTokenValue());
        dbObject.put("userName", source.getUsername());
        return dbObject;
    }
}
