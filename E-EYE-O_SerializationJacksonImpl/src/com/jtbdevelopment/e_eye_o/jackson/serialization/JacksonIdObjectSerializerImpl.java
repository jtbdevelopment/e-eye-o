package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer.ENTITY_TYPE_FIELD;
import static com.jtbdevelopment.e_eye_o.jackson.serialization.JacksonJSONIdObjectSerializer.ID_FIELD;

/**
 * Date: 1/27/13
 * Time: 10:00 PM
 */
@Service
public class JacksonIdObjectSerializerImpl implements JacksonIdObjectSerializer {
    private final IdObjectInterfaceResolver interfaceResolver;

    @Autowired
    public JacksonIdObjectSerializerImpl(final IdObjectInterfaceResolver interfaceResolver) {
        this.interfaceResolver = interfaceResolver;
    }

    @Override
    public void serialize(final IdObject value, final JsonGenerator generator) throws IOException {
        Class<? extends IdObject> valueInterface = interfaceResolver.getIdObjectInterfaceForClass(value.getClass());
        if (valueInterface == null) {
            throw new RuntimeException("Cannot resolve IdObjectInterface for object");
        }
        generator.writeStartObject();
        generator.writeStringField(ENTITY_TYPE_FIELD, valueInterface.getCanonicalName());
        for (Map.Entry<String, Class> field : interfaceResolver.getAllGetMethodReturns(valueInterface)) {
            final String fieldName = field.getKey();
            final Object fieldValue = getFieldValue(value, fieldName);
            final Class<?> valueType = field.getValue();

            generator.writeFieldName(fieldName);
            if (IdObject.class.isAssignableFrom(valueType)) {
                @SuppressWarnings("unchecked")
                Class<? extends IdObject> fieldValueClass = (Class<? extends IdObject>) (fieldValue == null ? valueType : fieldValue.getClass());
                writeSubEntity(generator, fieldValueClass, (IdObject) fieldValue);
            } else if (Set.class.isAssignableFrom(valueType)) {
                writeSet(generator, (Set) fieldValue);
            } else {
                generator.writeObject(fieldValue);
            }
        }
        generator.writeEndObject();
    }

    private void writeSet(final JsonGenerator jgen, final Set fieldValue) throws IOException {
        jgen.writeStartArray();
        for (Object object : fieldValue) {
            if (object instanceof IdObject) {
                @SuppressWarnings("unchecked")
                Class<? extends IdObject> objectClass = (Class<? extends IdObject>) (object.getClass());
                writeSubEntity(jgen, objectClass, (IdObject) object);
            } else {
                jgen.writeObject(object);
            }
        }
        jgen.writeEndArray();
    }

    private Object getFieldValue(final IdObject value, final String fieldName) {
        try {
            return PropertyUtils.getSimpleProperty(value, fieldName);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSubEntity(final JsonGenerator jgen, final Class<? extends IdObject> entityType, final IdObject entity) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(ENTITY_TYPE_FIELD, interfaceResolver.getIdObjectInterfaceForClass(entityType).getCanonicalName());
        jgen.writeStringField(ID_FIELD, entity == null ? null : entity.getId());
        jgen.writeEndObject();
    }

}
