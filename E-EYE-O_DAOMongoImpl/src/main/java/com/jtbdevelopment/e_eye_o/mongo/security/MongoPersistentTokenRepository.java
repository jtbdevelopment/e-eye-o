package com.jtbdevelopment.e_eye_o.mongo.security;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Date: 1/14/14
 * Time: 6:52 AM
 */
@Component("persistentTokenRepository")
public class MongoPersistentTokenRepository implements PersistentTokenRepository {
    @Autowired
    private DBCollection cookiesCollection;

    @Autowired
    private MongoPersistentRememberMeTokenReadConverter rememberMeTokenReadConverter;

    @Autowired
    private MongoPersistentRememberMeTokenWriteConverter rememberMeTokenWriteConverter;

    @Override
    public void createNewToken(final PersistentRememberMeToken token) {
        cookiesCollection.insert(rememberMeTokenWriteConverter.convert(token));
    }

    @Override
    public void updateToken(final String series, final String tokenValue, final Date lastUsed) {
        PersistentRememberMeToken read = getTokenForSeries(series);
        PersistentRememberMeToken write = new PersistentRememberMeToken(
                read.getUsername(),
                series,
                tokenValue,
                lastUsed
        );
        cookiesCollection.update(createIdQuery(series), rememberMeTokenWriteConverter.convert(write));
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(final String seriesId) {
        return rememberMeTokenReadConverter.convert((BasicDBObject) cookiesCollection.findOne(createIdQuery(seriesId)));
    }

    protected BasicDBObject createIdQuery(final String seriesId) {
        return new BasicDBObject("series", seriesId);
    }

    @Override
    public void removeUserTokens(final String username) {
        cookiesCollection.remove(new BasicDBObject("userName", username));
    }
}
