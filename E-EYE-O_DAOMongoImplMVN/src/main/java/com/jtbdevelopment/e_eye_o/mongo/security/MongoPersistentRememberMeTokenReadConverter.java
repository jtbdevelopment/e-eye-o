package com.jtbdevelopment.e_eye_o.mongo.security;

import com.mongodb.BasicDBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.stereotype.Component;

/**
 * Date: 1/26/14
 * Time: 11:23 AM
 */
@Component
public class MongoPersistentRememberMeTokenReadConverter implements Converter<BasicDBObject, PersistentRememberMeToken> {
    @Override
    public PersistentRememberMeToken convert(final BasicDBObject source) {
        if (source == null) {
            return null;
        }

        PersistentRememberMeToken token = new PersistentRememberMeToken(
                source.getString("userName"),
                source.getString("series"),
                source.getString("tokenValue"),
                source.getDate("date")
        );
        return token;
    }
}
