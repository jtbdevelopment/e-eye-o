package com.jtbdevelopment.e_eye_o.jersey.rest.converters;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Date: 11/8/13
 * Time: 7:03 PM
 */
@Provider
@Service
@Produces(MediaType.APPLICATION_JSON)
public class IdObjectMessageWriter<T extends IdObject> extends AbstractMessageBodyWriter<T> {
    @Autowired
    private IdObjectSerializer idObjectSerializer;

    @Override
    public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return IdObject.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(final T t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
        writeStringToStreamAsUTF8(idObjectSerializer.writeEntity(t), entityStream);
    }
}
