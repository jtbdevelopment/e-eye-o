package com.jtbdevelopment.e_eye_o.jersey.rest.converters;

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
import java.util.Map;

/**
 * Date: 11/9/13
 * Time: 10:12 AM
 */
@Provider
@Service
@Produces(MediaType.APPLICATION_JSON)
public class PaginatedMessageWriter extends AbstractMessageBodyWriter<Map<String, Object>> {
    @Autowired
    private IdObjectSerializer idObjectSerializer;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return (Map.class.isAssignableFrom(type));
    }

    @Override
    public void writeTo(Map<String, Object> stringObjectMap, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        writeStringToStreamAsUTF8(idObjectSerializer.writeMap(stringObjectMap), entityStream);
    }
}

