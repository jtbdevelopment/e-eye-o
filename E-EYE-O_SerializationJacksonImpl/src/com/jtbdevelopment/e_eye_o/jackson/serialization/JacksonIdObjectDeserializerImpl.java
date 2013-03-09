package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.BigIntegerStringConverter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer.ENTITY_TYPE_FIELD;
import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer.ID_FIELD;

/**
 * Date: 2/2/13
 * Time: 10:55 PM
 */
@Service
public class JacksonIdObjectDeserializerImpl implements JacksonIdObjectDeserializer {
    private final ReadOnlyDAO readOnlyDAO;
    private final IdObjectFactory idObjectFactory;
    private final IdObjectInterfaceResolver interfaceResolver;

    @Autowired
    public JacksonIdObjectDeserializerImpl(final ReadOnlyDAO readOnlyDAO, final IdObjectFactory idObjectFactory, final IdObjectInterfaceResolver interfaceResolver) {
        this.readOnlyDAO = readOnlyDAO;
        this.idObjectFactory = idObjectFactory;
        this.interfaceResolver = interfaceResolver;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IdObject deserialize(final JsonParser parser) throws IOException {
        checkStartingPosition(parser);
        JsonToken currentToken = parser.nextToken();

        IdObject returnObject = null;
        Class<? extends IdObject> returnInterface = null;
        try {
            String fieldName = null;
            Class fieldType = null;
            while (currentToken != JsonToken.END_OBJECT) {
                Method setMethod = null;
                if (fieldType != null) {
                    setMethod = interfaceResolver.getSetMethod(returnInterface, fieldName, fieldType);
                }
                switch (currentToken) {
                    case FIELD_NAME:
                        fieldName = parser.getCurrentName();
                        if (!ENTITY_TYPE_FIELD.equals(fieldName)) {
                            fieldType = interfaceResolver.getIsOrGetMethod(returnInterface, fieldName).getReturnType();
                        }
                        break;
                    case VALUE_STRING:
                        if (ENTITY_TYPE_FIELD.equals(fieldName)) {
                            try {
                                returnObject = idObjectFactory.newIdObject((Class<? extends IdObject>) Class.forName(parser.getValueAsString()));
                                returnInterface = interfaceResolver.getIdObjectInterfaceForClass(returnObject.getClass());
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            assignValue(returnObject, setMethod, parser.getValueAsString());
                        }
                        break;
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                        handleBoolean(parser, returnObject, setMethod);
                        break;
                    case VALUE_NUMBER_FLOAT:
                        handleFloat(parser, returnObject, fieldType, setMethod);
                        break;
                    case VALUE_NUMBER_INT:
                        handleInteger(parser, returnObject, fieldType, setMethod);
                        break;
                    case START_OBJECT:
                        handleStartObject(parser, returnObject, fieldType, setMethod);
                        break;
                    case START_ARRAY:
                        handleStartArray(parser, returnObject, fieldType, setMethod);
                        break;
                    default:
                }
                currentToken = parser.nextToken();
            }
            return returnObject;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkStartingPosition(final JsonParser parser) {
        JsonToken currentToken = parser.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException("Not positioned at start");
        }
    }

    private void handleFloat(final JsonParser parser, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IllegalAccessException, InvocationTargetException, IOException {
        if (BigDecimal.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, setMethod, new BigDecimalStringConverter().fromString(parser.getValueAsString()));
        } else {
            assignValue(returnObject, setMethod, parser.getValueAsDouble());
        }
    }

    private void handleBoolean(final JsonParser parser, final IdObject returnObject, final Method setMethod) throws IllegalAccessException, InvocationTargetException, IOException {
        assignValue(returnObject, setMethod, parser.getValueAsBoolean());
    }

    private void handleInteger(final JsonParser parser, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IllegalAccessException, InvocationTargetException, IOException {
        if (DateTime.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, setMethod, new DateTime(parser.getValueAsLong()));
        } else if (BigInteger.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, setMethod, new BigIntegerStringConverter().fromString(parser.getValueAsString()));
        } else {
            if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                assignValue(returnObject, setMethod, parser.getValueAsLong());
            } else {
                assignValue(returnObject, setMethod, parser.getValueAsInt());
            }
        }
    }

    private void handleStartArray(final JsonParser parser, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IOException, IllegalAccessException, InvocationTargetException {
        if (fieldType != null && Set.class.isAssignableFrom(fieldType)) {
            Set objects = readSet(parser);
            assignValue(returnObject, setMethod, objects);
        } else {
            Object value = deserializeObject(fieldType, parser);
            assignValue(returnObject, setMethod, value);
        }
    }

    private void assignValue(final IdObject returnObject, final Method setMethod, final Object value) throws IllegalAccessException, InvocationTargetException {
        if (value != null && setMethod != null) {
            setMethod.invoke(returnObject, value);
        }
    }

    private void handleStartObject(final JsonParser parser, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IOException, IllegalAccessException, InvocationTargetException {
        if (fieldType != null && IdObject.class.isAssignableFrom(fieldType)) {
            IdObject object = readSubObject(parser);
            if (object != null) {
                setMethod.invoke(returnObject, object);
            }
        } else {
            Object value = deserializeObject(fieldType, parser);
            if (value != null) {
                setMethod.invoke(returnObject, value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object deserializeObject(final Class fieldType, final JsonParser parser) throws IOException {
        return parser.readValueAs(fieldType);
    }

    @SuppressWarnings("unchecked")
    private IdObject readSubObject(final JsonParser parser) throws IOException {
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
                return readOnlyDAO.get((Class<? extends IdObject>) Class.forName(subObjectType), subObjectId);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private Set<IdObject> readSet(final JsonParser parser) throws IOException {
        JsonToken currentToken = parser.getCurrentToken();
        Set<IdObject> returnSet = new HashSet<>();
        while (currentToken != JsonToken.END_ARRAY) {
            switch (currentToken) {
                case START_OBJECT:
                    returnSet.add(readSubObject(parser));
                    break;
                default:
            }
            currentToken = parser.nextToken();
        }
        return returnSet;
    }
}
