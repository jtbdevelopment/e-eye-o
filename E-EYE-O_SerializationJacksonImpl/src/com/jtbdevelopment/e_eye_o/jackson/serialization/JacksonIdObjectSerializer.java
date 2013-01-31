package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.helpers.IdObjectInterfaceResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Date: 1/27/13
 * Time: 10:00 PM
 */
@Service
public class JacksonIdObjectSerializer extends StdSerializer<IdObject> implements ResolvableSerializer {
    private static final String BLANK = "";
    private static final String ENTITY_TYPE = "entityType";
    private static final String ID = "id";
    private final IdObjectInterfaceResolver interfaceResolver;

    @Autowired
    public JacksonIdObjectSerializer(final IdObjectInterfaceResolver interfaceResolver) {
        super(IdObject.class);
        this.interfaceResolver = interfaceResolver;
    }

    @Override
    public void serialize(final IdObject value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        Class valueInterface = interfaceResolver.getIdObjectInterfaceForClass(value.getClass());
        if (valueInterface == null) {
            throw new RuntimeException("Cannot resolve IdObjectInterface for object");
        }
        jgen.writeStartObject();
        jgen.writeStringField(ENTITY_TYPE, valueInterface.getSimpleName());
        for (Method method : interfaceResolver.getAllGetters(value.getClass())) {
            String field = method.getName();
            if (method.getName().startsWith(IdObjectInterfaceResolver.GET)) {
                field = field.replaceFirst(IdObjectInterfaceResolver.GET, BLANK);
            } else if (field.startsWith(IdObjectInterfaceResolver.IS)) {
                field = field.replaceFirst(IdObjectInterfaceResolver.IS, BLANK);
            }
            field = StringUtils.uncapitalize(field);
            jgen.writeFieldName(field);
            final Class<?> returnType = method.getReturnType();
            final Object returnValue;
            try {
                returnValue = method.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if (IdObject.class.isAssignableFrom(returnType)) {
                jgen.writeStartObject();
                final IdObject idObject = (IdObject) returnValue;
                jgen.writeStringField(ENTITY_TYPE, interfaceResolver.getIdObjectInterfaceForClass(idObject.getClass()).getSimpleName());
                jgen.writeStringField(ID, idObject.getId());
                jgen.writeEndObject();
            } else if (Set.class.isAssignableFrom(returnType)) {
                jgen.writeStartArray();
                for (Object object : (Set) returnValue) {
                    if (object instanceof IdObject) {
                        jgen.writeStartObject();
                        final IdObject setIdObject = (IdObject) object;
                        jgen.writeStringField(ENTITY_TYPE, interfaceResolver.getIdObjectInterfaceForClass(setIdObject.getClass()).getSimpleName());
                        jgen.writeStringField("id", setIdObject.getId());
                        jgen.writeEndObject();
                    }
                }
                jgen.writeEndArray();
            } else {
                jgen.writeObject(returnValue);
            }
        }
        jgen.writeEndObject();
    }

    @Override
    public void resolve(final SerializerProvider provider) throws JsonMappingException {

    }
/*
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider prov, final BeanProperty property) throws JsonMappingException {
        return null;
    }
    */
}
