package com.jtbdevelopment.e_eye_o.jersey.rest.converters;

import com.google.common.io.CharStreams;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Date: 11/5/13
 * Time: 7:16 PM
 */
@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Service
public class IdObjectMessageReader<T extends IdObject> implements MessageBodyReader<T> {
    @Autowired
    IdObjectSerializer idObjectSerializer;

    @Override
    public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return IdObject.class.isAssignableFrom(type);
    }

    @Override
    public T readFrom(final Class<T> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException, WebApplicationException {
        Object read = idObjectSerializer.read(CharStreams.toString(new InputStreamReader(entityStream)));
        if (type.isAssignableFrom(read.getClass())) {
            return (T) read;
        }
        throw new IllegalArgumentException(read.getClass() + " not an AppUserOwnedObject");
    }
}
