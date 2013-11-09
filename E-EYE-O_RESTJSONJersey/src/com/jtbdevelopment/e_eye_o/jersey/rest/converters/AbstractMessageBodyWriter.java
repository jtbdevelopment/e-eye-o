package com.jtbdevelopment.e_eye_o.jersey.rest.converters;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Date: 11/9/13
 * Time: 11:34 AM
 */
public abstract class AbstractMessageBodyWriter<T> implements MessageBodyWriter<T> {
    protected void writeStringToStreamAsUTF8(final String string, final OutputStream outputStream) throws IOException {
        outputStream.write(string.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    @Override
    public long getSize(T t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}
