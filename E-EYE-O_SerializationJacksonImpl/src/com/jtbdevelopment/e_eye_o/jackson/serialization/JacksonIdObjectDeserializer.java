package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonIdObjectConstants.ENTITY_TYPE_FIELD;
import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonIdObjectConstants.ID_FIELD;

/**
 * Date: 2/2/13
 * Time: 10:55 PM
 */
@Service
public class JacksonIdObjectDeserializer<T extends IdObject> extends StdDeserializer<T> {
    private final ReadOnlyDAO readOnlyDAO;
    private final IdObjectFactory idObjectFactory;

    @Autowired
    public JacksonIdObjectDeserializer(final ReadOnlyDAO readOnlyDAO, final IdObjectFactory idObjectFactory) {
        super(IdObject.class);
        this.readOnlyDAO = readOnlyDAO;
        this.idObjectFactory = idObjectFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken currentToken = jp.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException("Not positioned at start");
        }

        String objectType = null;
        String fieldName = null;
        IdObject returnObject = null;
        currentToken = jp.nextToken();

        while (currentToken != JsonToken.END_OBJECT) {
            switch (currentToken) {
                case FIELD_NAME:
                    fieldName = jp.getCurrentName();
                    break;
                case VALUE_STRING:
                    if(ENTITY_TYPE_FIELD.equals(fieldName)) {
                        try {
                            returnObject = idObjectFactory.newIdObject((Class<? extends IdObject>) Class.forName(jp.getValueAsString()));
                        } catch (ClassNotFoundException e) {
                            throw ctxt.instantiationException(IdObject.class, e);
                        }
                    }
                    break;
                case START_OBJECT:
                    IdObject object = readSubObject(jp, ctxt);
                    break;
                case START_ARRAY:
                    Set objects = readArray(jp, ctxt);
                    break;
                default:
            }
            currentToken = jp.nextToken();
        }
        return (T) returnObject;
    }

    private IdObject readSubObject(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken currentToken = jp.getCurrentToken();
        String subObjectType = null;
        String subObjectId = null;
        String field = null;
        while (currentToken != JsonToken.END_OBJECT) {
            switch (currentToken) {
                case FIELD_NAME:
                    field = jp.getCurrentName();
                    break;
                case VALUE_STRING:
                    if (ENTITY_TYPE_FIELD.equals(field)) {
                        subObjectType = jp.getValueAsString();
                    } else if (ID_FIELD.equals(field)) {
                        subObjectId = jp.getValueAsString();
                    }
                    break;
                default:
            }
            currentToken = jp.nextToken();
        }

        if (subObjectType != null && subObjectId != null) {
            try {
                return readOnlyDAO.get((Class<? extends IdObject>) Class.forName(subObjectType), subObjectId);
            } catch (ClassNotFoundException e) {
                throw ctxt.instantiationException(IdObject.class, e);
            }
        }
        return null;
    }

    private Set<IdObject> readArray(final JsonParser jp, final DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken currentToken = jp.getCurrentToken();
        Set<IdObject> returnSet = new HashSet<>();
        while (currentToken != JsonToken.END_ARRAY) {
            switch (currentToken) {
                case START_OBJECT:
                    returnSet.add(readSubObject(jp, ctxt));
                    break;
                default:
            }
            currentToken = jp.nextToken();
        }
        return returnSet;
    }
}
