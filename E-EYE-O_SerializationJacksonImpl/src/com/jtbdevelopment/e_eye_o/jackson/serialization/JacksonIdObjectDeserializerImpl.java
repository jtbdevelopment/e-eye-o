package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer.ENTITY_TYPE_FIELD;
import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer.ID_FIELD;

/**
 * Date: 2/2/13
 * Time: 10:55 PM
 */
@Service
@SuppressWarnings("unused")
public class JacksonIdObjectDeserializerImpl implements JacksonIdObjectDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(JacksonIdObjectDeserializer.class);
    private final ReadOnlyDAO readOnlyDAO;
    private final IdObjectFactory idObjectFactory;
    private final PhotoHelper photoHelper;

    @Autowired
    public JacksonIdObjectDeserializerImpl(final ReadOnlyDAO readOnlyDAO, final IdObjectFactory idObjectFactory, final PhotoHelper photoHelper) {
        this.readOnlyDAO = readOnlyDAO;
        this.idObjectFactory = idObjectFactory;
        this.photoHelper = photoHelper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IdObject deserialize(final JsonParser parser) throws IOException {
        checkStartingPosition(parser);
        JsonToken currentToken = parser.nextToken();

        IdObject returnObject = null;
        try {
            String fieldName = null;
            Class fieldType = null;
            while (currentToken != JsonToken.END_OBJECT) {
                switch (currentToken) {
                    case FIELD_NAME:
                        fieldName = parser.getCurrentName();
                        if (!ENTITY_TYPE_FIELD.equals(fieldName)) {
                            PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(returnObject, fieldName);
                            fieldType = propertyDescriptor.getPropertyType();
                        }
                        break;
                    case VALUE_STRING:
                        if (ENTITY_TYPE_FIELD.equals(fieldName)) {
                            try {
                                returnObject = idObjectFactory.newIdObject((Class<? extends IdObject>) Class.forName(parser.getValueAsString()));
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            handleString(parser, returnObject, fieldType, fieldName);
                        }
                        break;
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                        handleBoolean(parser, returnObject, fieldName);
                        break;
                    case VALUE_NUMBER_FLOAT:
                        handleFloat(parser, returnObject, fieldType, fieldName);
                        break;
                    case VALUE_NUMBER_INT:
                        handleInteger(parser, returnObject, fieldType, fieldName);
                        break;
                    case START_OBJECT:
                        handleStartObject(parser, returnObject, fieldType, fieldName);
                        break;
                    case START_ARRAY:
                        handleStartArray(parser, returnObject, fieldType, fieldName);
                        break;
                    default:
                }
                currentToken = parser.nextToken();
            }
            return returnObject;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkStartingPosition(final JsonParser parser) {
        JsonToken currentToken = parser.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException("Not positioned at start");
        }
    }

    private void handleString(JsonParser parser, IdObject returnObject, final Class fieldType, String fieldName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (fieldType.isArray()) {
            if (byte.class.isAssignableFrom(fieldType.getComponentType())) {
                String s = parser.getValueAsString();
                byte[] decoded = Base64.decode(s.getBytes());
                if ("imageData".equals(fieldName) && returnObject instanceof Photo) {
                    photoHelper.setPhotoImages((Photo) returnObject, decoded);
                } else {
                    assignValue(returnObject, fieldName, decoded);
                }
                return;
            }
        }
        if (Enum.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, fieldName, Enum.valueOf(fieldType, parser.getValueAsString()));
            return;
        }
        assignValue(returnObject, fieldName, parser.getValueAsString());
        if ("mimeType".equals(fieldName) && returnObject instanceof Photo) {
            photoHelper.reprocessForMimeType((Photo) returnObject);
        }
    }

    private void handleFloat(final JsonParser parser, final IdObject returnObject, final Class fieldType, final String fieldName) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        assignValue(returnObject, fieldName, parser.getValueAsDouble());
    }

    private void handleBoolean(final JsonParser parser, final IdObject returnObject, final String fieldName) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        assignValue(returnObject, fieldName, parser.getValueAsBoolean());
    }

    private void handleInteger(final JsonParser parser, final IdObject returnObject, final Class fieldType, final String fieldName) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        if (DateTime.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, fieldName, new DateTime(parser.getValueAsLong()));
        } else {
            if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                assignValue(returnObject, fieldName, parser.getValueAsLong());
            } else {
                assignValue(returnObject, fieldName, parser.getValueAsInt());
            }
        }
    }

    private void handleStartArray(final JsonParser parser, final IdObject returnObject, final Class fieldType, final String fieldName) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object value;
        if (fieldType != null && Set.class.isAssignableFrom(fieldType)) {
            value = readSet(parser, fieldName);
        } else {
            value = deserializeObject(fieldType, parser);
        }

        assignValue(returnObject, fieldName, value);
    }

    private void handleStartObject(final JsonParser parser, final IdObject returnObject, final Class fieldType, final String fieldName) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object value;
        if (fieldType != null && IdObject.class.isAssignableFrom(fieldType)) {
            value = readSubObject(parser, fieldName, fieldType);
        } else {
            value = deserializeObject(fieldType, parser);
        }
        assignValue(returnObject, fieldName, value);
    }

    @SuppressWarnings("unchecked")
    private Object deserializeObject(final Class fieldType, final JsonParser parser) throws IOException {
        return parser.readValueAs(fieldType);
    }

    @SuppressWarnings("unchecked")
    private IdObject readSubObject(final JsonParser parser, final String subObjectField, final Class expectedSubObjectType) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        String subObjectType = null;
        String subObjectId = null;
        String field = null;
        while (currentToken != JsonToken.END_OBJECT) {
            switch (currentToken) {
                case FIELD_NAME:
                    field = parser.getCurrentName();
                    break;
                case VALUE_STRING:
                    if (ENTITY_TYPE_FIELD.equals(field)) {
                        subObjectType = parser.getValueAsString();
                    } else if (ID_FIELD.equals(field)) {
                        subObjectId = parser.getValueAsString();
                    }
                    break;
                default:
            }
            currentToken = parser.nextToken();
        }

        if (subObjectType != null && subObjectId != null) {
            try {
                IdObject o = readOnlyDAO.get((Class<? extends IdObject>) Class.forName(subObjectType), subObjectId);
                if (o == null) {
                    logger.warn("Load idObject for field {" + subObjectField +
                            "} of class {" + subObjectType +
                            "} and id {" + subObjectId + "} returned null");
                }
                return o;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("While loading field {" +
                subObjectField + "} of expected type {" +
                expectedSubObjectType.getCanonicalName() + "}, either type {" +
                subObjectType + "} or Id {" + subObjectId + "} were null.");
    }

    private Set<IdObject> readSet(final JsonParser parser, final String fieldName) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        Set<IdObject> returnSet = new HashSet<>();
        while (currentToken != JsonToken.END_ARRAY) {
            switch (currentToken) {
                case START_OBJECT:
                    returnSet.add(readSubObject(parser, fieldName, Object.class));
                    break;
                default:
            }
            currentToken = parser.nextToken();
        }
        return returnSet;
    }

    private void assignValue(final IdObject returnObject, final String fieldName, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (value != null && fieldName != null) {
            PropertyUtils.setSimpleProperty(returnObject, fieldName, value);
        }
    }
}
