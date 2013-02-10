package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
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
    private final IdObjectInterfaceResolver interfaceResolver;

    @Autowired
    public JacksonIdObjectDeserializer(final ReadOnlyDAO readOnlyDAO, final IdObjectFactory idObjectFactory, final IdObjectInterfaceResolver interfaceResolver) {
        super(IdObject.class);
        this.readOnlyDAO = readOnlyDAO;
        this.idObjectFactory = idObjectFactory;
        this.interfaceResolver = interfaceResolver;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
        checkStartingPosition(jp);
        JsonToken currentToken = jp.nextToken();

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
                        fieldName = jp.getCurrentName();
                        if (!ENTITY_TYPE_FIELD.equals(fieldName)) {
                            fieldType = interfaceResolver.getIsOrGetMethod(returnInterface, fieldName).getReturnType();
                        }
                        break;
                    case VALUE_STRING:
                        if (ENTITY_TYPE_FIELD.equals(fieldName)) {
                            try {
                                returnObject = idObjectFactory.newIdObject((Class<? extends IdObject>) Class.forName(jp.getValueAsString()));
                                returnInterface = interfaceResolver.getIdObjectInterfaceForClass(returnObject.getClass());
                            } catch (ClassNotFoundException e) {
                                throw ctxt.instantiationException(IdObject.class, e);
                            }
                        } else {
                            assignValue(returnObject, setMethod, jp.getValueAsString());
                        }
                        break;
                    case VALUE_FALSE:
                    case VALUE_TRUE:
                        handleBoolean(jp, returnObject, setMethod);
                        break;
                    case VALUE_NUMBER_FLOAT:
                        handleFloat(jp, returnObject, fieldType, setMethod);
                        break;
                    case VALUE_NUMBER_INT:
                        handleInteger(jp, returnObject, fieldType, setMethod);
                        break;
                    case START_OBJECT:
                        handleStartObject(jp, ctxt, returnObject, fieldType, setMethod);
                        break;
                    case START_ARRAY:
                        handleStartArray(jp, ctxt, returnObject, fieldType, setMethod);
                        break;
                    default:
                }
                currentToken = jp.nextToken();
            }
            return (T) returnObject;
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkStartingPosition(final JsonParser jp) {
        JsonToken currentToken = jp.getCurrentToken();
        if (currentToken != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException("Not positioned at start");
        }
    }

    private void handleFloat(final JsonParser jp, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IllegalAccessException, InvocationTargetException, IOException {
        if (BigDecimal.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, setMethod, new BigDecimalStringConverter().fromString(jp.getValueAsString()));
        } else {
            assignValue(returnObject, setMethod, jp.getValueAsDouble());
        }
    }

    private void handleBoolean(final JsonParser jp, final IdObject returnObject, final Method setMethod) throws IllegalAccessException, InvocationTargetException, IOException {
        assignValue(returnObject, setMethod, jp.getValueAsBoolean());
    }

    private void handleInteger(final JsonParser jp, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IllegalAccessException, InvocationTargetException, IOException {
        if (DateTime.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, setMethod, new DateTime(jp.getValueAsLong()));
        } else if (BigInteger.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, setMethod, new BigIntegerStringConverter().fromString(jp.getValueAsString()));
        } else {
            if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                assignValue(returnObject, setMethod, jp.getValueAsLong());
            } else {
                assignValue(returnObject, setMethod, jp.getValueAsInt());
            }
        }
    }

    private void handleStartArray(final JsonParser jp, final DeserializationContext ctxt, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IOException, IllegalAccessException, InvocationTargetException {
        if (fieldType != null && Set.class.isAssignableFrom(fieldType)) {
            Set objects = readSet(jp, ctxt);
            assignValue(returnObject, setMethod, objects);
        } else {
            Object value = deserializeObject(fieldType, jp, ctxt);
            assignValue(returnObject, setMethod, value);
        }
    }

    private void assignValue(final IdObject returnObject, final Method setMethod, final Object value) throws IllegalAccessException, InvocationTargetException {
        if (value != null && setMethod != null) {
            setMethod.invoke(returnObject, value);
        }
    }

    private void handleStartObject(final JsonParser jp, final DeserializationContext ctxt, final IdObject returnObject, final Class fieldType, final Method setMethod) throws IOException, IllegalAccessException, InvocationTargetException {
        if (fieldType != null && IdObject.class.isAssignableFrom(fieldType)) {
            IdObject object = readSubObject(jp, ctxt);
            if (object != null) {
                setMethod.invoke(returnObject, object);
            }
        } else {
            Object value = deserializeObject(fieldType, jp, ctxt);
            if (value != null) {
                setMethod.invoke(returnObject, value);
            }
        }
    }

    private Object deserializeObject(final Class fieldType, final JsonParser jp, final DeserializationContext context) throws IOException {
        JsonDeserializer deserializer = context.findRootValueDeserializer(context.constructType(fieldType));
        if (deserializer != null) {
            return deserializer.deserialize(jp, context);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private IdObject readSubObject(final JsonParser jp, final DeserializationContext context) throws IOException {
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
                throw context.instantiationException(IdObject.class, e);
            }
        }
        return null;
    }

    private Set<IdObject> readSet(final JsonParser jp, final DeserializationContext context) throws IOException {
        JsonToken currentToken = jp.getCurrentToken();
        Set<IdObject> returnSet = new HashSet<>();
        while (currentToken != JsonToken.END_ARRAY) {
            switch (currentToken) {
                case START_OBJECT:
                    returnSet.add(readSubObject(jp, context));
                    break;
                default:
            }
            currentToken = jp.nextToken();
        }
        return returnSet;
    }
}
