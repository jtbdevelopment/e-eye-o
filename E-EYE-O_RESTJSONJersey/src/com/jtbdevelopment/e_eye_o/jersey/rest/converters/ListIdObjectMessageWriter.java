package com.jtbdevelopment.e_eye_o.jersey.rest.converters;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
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
import java.util.Collection;

/**
 * Date: 11/9/13
 * Time: 11:30 AM
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Service
public class ListIdObjectMessageWriter extends AbstractMessageBodyWriter<Collection<? extends IdObject>> {
    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return Collection.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(Collection<? extends IdObject> idObjects, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        writeStringToStreamAsUTF8(jsonIdObjectSerializer.write(idObjects), entityStream);
    }
}
