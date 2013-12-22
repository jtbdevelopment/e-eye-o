package com.jtbdevelopment.e_eye_o.jersey.rest.converters;

import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList;
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
 * Date: 11/9/13
 * Time: 10:12 AM
 */
@Provider
@Service
@Produces(MediaType.APPLICATION_JSON)
public class PaginatedIdObjectListWriter extends AbstractMessageBodyWriter<PaginatedIdObjectList> {
    @Autowired
    private IdObjectSerializer idObjectSerializer;

    @Override
    public boolean isWriteable(final Class<?> type, Type genericType, final Annotation[] annotations, final MediaType mediaType) {
        return (PaginatedIdObjectList.class.isAssignableFrom(type));
    }

    @Override
    public void writeTo(final PaginatedIdObjectList paginatedIdObjectList,
                        final Class<?> type,
                        final Type genericType,
                        final Annotation[] annotations,
                        final MediaType mediaType,
                        final MultivaluedMap<String, Object> httpHeaders,
                        final OutputStream entityStream) throws IOException, WebApplicationException {
        writeStringToStreamAsUTF8(idObjectSerializer.write(paginatedIdObjectList), entityStream);
    }
}

