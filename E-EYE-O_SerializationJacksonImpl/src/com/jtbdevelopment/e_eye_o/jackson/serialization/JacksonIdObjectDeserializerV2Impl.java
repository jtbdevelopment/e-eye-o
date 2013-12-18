package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.PhotoHelper;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializerV2.ENTITY_TYPE_FIELD;
import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializerV2.ID_FIELD;

/**
 * Date: 12/17/13
 * Time: 11:55 AM
 */
@Component
@SuppressWarnings("unused")
public class JacksonIdObjectDeserializerV2Impl implements JacksonIdObjectDeserializerV2 {
    private static final Logger logger = LoggerFactory.getLogger(JacksonIdObjectDeserializerV2Impl.class);
    private final ReadOnlyDAO readOnlyDAO;
    private final IdObjectFactory idObjectFactory;
    private final PhotoHelper photoHelper;

    @Autowired
    public JacksonIdObjectDeserializerV2Impl(final ReadOnlyDAO readOnlyDAO, final IdObjectFactory idObjectFactory, final PhotoHelper photoHelper) {
        this.readOnlyDAO = readOnlyDAO;
        this.idObjectFactory = idObjectFactory;
        this.photoHelper = photoHelper;
    }

    @Override
    public <T extends IdObject> T deserialize(final Map<String, Object> values) throws IOException {
        try {
            T returnObject;
            if (values.containsKey(ENTITY_TYPE_FIELD)) {
                Object o = values.get(ENTITY_TYPE_FIELD);
                try {
                    returnObject = (T) idObjectFactory.newIdObject((Class<? extends IdObject>) Class.forName((String) o));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("No " + ENTITY_TYPE_FIELD + " field in map");
            }

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                if (ENTITY_TYPE_FIELD.equals(entry.getKey())) {
                    continue;
                }
                String fieldName = entry.getKey();
                PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(returnObject, fieldName);
                Class fieldType = propertyDescriptor.getPropertyType();
                Object value = entry.getValue();
                if (value instanceof Map) {
                    handleStartObject(returnObject, fieldType, fieldName, (Map) value);
                } else if (value instanceof List) {
                    handleStartArray(returnObject, fieldType, fieldName, (List) value);
                } else if (value instanceof String) {
                    handleString(returnObject, fieldType, fieldName, (String) value);
                } else if (value instanceof Boolean) {
                    handleBoolean(returnObject, fieldName, (Boolean) value);
                } else if (value instanceof Float) {
                    handleFloat(returnObject, fieldType, fieldName, (Float) value);
                } else if (value instanceof Number) {
                    handleInteger(returnObject, fieldType, fieldName, (Number) value);
                }
            }
            return returnObject;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleString(final IdObject returnObject, final Class fieldType, final String fieldName, final String value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        if (fieldType.isArray()) {
            if (byte.class.isAssignableFrom(fieldType.getComponentType())) {
                byte[] decoded = Base64.decode(value.getBytes());
                if ("imageData".equals(fieldName) && returnObject instanceof Photo) {
                    photoHelper.setPhotoImages((Photo) returnObject, decoded);
                } else {
                    assignValue(returnObject, fieldName, decoded);
                }
                return;
            }
        }
        if (Enum.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, fieldName, Enum.valueOf(fieldType, value));
            return;
        }
        assignValue(returnObject, fieldName, value);
        if ("mimeType".equals(fieldName) && returnObject instanceof Photo) {
            photoHelper.reprocessForMimeType((Photo) returnObject);
        }
    }

    private void handleFloat(final IdObject returnObject, final Class fieldType, final String fieldName, final Float value) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        assignValue(returnObject, fieldName, value);
    }

    private void handleBoolean(final IdObject returnObject, final String fieldName, final Boolean value) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        assignValue(returnObject, fieldName, value);
    }

    private void handleInteger(final IdObject returnObject, final Class fieldType, final String fieldName, final Number value) throws IllegalAccessException, InvocationTargetException, IOException, NoSuchMethodException {
        if (DateTime.class.isAssignableFrom(fieldType)) {
            assignValue(returnObject, fieldName, new DateTime(value));
        } else {
            if (long.class.isAssignableFrom(fieldType) || Long.class.isAssignableFrom(fieldType)) {
                assignValue(returnObject, fieldName, value);
            } else {
                assignValue(returnObject, fieldName, value);
            }
        }
    }

    private void handleStartArray(final IdObject returnObject, final Class fieldType, final String fieldName, final List values) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object assign;
        if (fieldType != null && Set.class.isAssignableFrom(fieldType)) {
            assign = readSet(fieldName, (List<Map<String, Object>>) values);
        } else {
            if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                List<Integer> valuesLong = (List<Integer>) values;
                assign = new LocalDateTime(
                        valuesLong.get(0),
                        valuesLong.get(1),
                        valuesLong.get(2),
                        valuesLong.get(3),
                        valuesLong.get(4),
                        valuesLong.get(5),
                        valuesLong.get(6));
            } else {
                assign = values;//   TODO - use case?
            }
        }

        assignValue(returnObject, fieldName, assign);
    }

    private void handleStartObject(final IdObject returnObject, final Class fieldType, final String fieldName, final Map value) throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object assign;
        if (fieldType != null && IdObject.class.isAssignableFrom(fieldType)) {
            assign = readSubObject(fieldName, fieldType, (Map<String, Object>) value);
        } else {
            assign = value;  //  TODO - what case is this?
        }
        assignValue(returnObject, fieldName, assign);
    }

    @SuppressWarnings("unchecked")
    private IdObject readSubObject(final String subObjectField, final Class expectedSubObjectType, final Map<String, Object> value) throws IOException {
        String subObjectType = (String) value.get(ENTITY_TYPE_FIELD);
        String subObjectId = (String) value.get(ID_FIELD);

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

    private Set<IdObject> readSet(final String fieldName, final List<Map<String, Object>> values) throws IOException {
        Set<IdObject> returnSet = new HashSet<>();
        for (Map<String, Object> value : values) {
            returnSet.add(readSubObject(fieldName, Object.class, value));
        }
        return returnSet;
    }

    private void assignValue(final IdObject returnObject, final String fieldName, final Object value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (value != null && fieldName != null) {
            PropertyUtils.setSimpleProperty(returnObject, fieldName, value);
        }
    }
}
