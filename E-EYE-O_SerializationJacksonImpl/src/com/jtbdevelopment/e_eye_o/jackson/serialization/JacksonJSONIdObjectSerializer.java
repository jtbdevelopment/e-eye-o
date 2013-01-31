package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.IdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Date: 1/26/13
 * Time: 10:35 PM
 */
@Service
public class JacksonJSONIdObjectSerializer implements IdObjectSerializer {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public JacksonJSONIdObjectSerializer(final JacksonIdObjectModule idObjectModule) {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(new JodaModule());
        mapper.registerModule(idObjectModule);
    }

    @Override
    public String write(final Object entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends IdObject> T read(final String input, Class<T> entityType) {
        try {
            return mapper.readValue(input, entityType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
