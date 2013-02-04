package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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

    @SuppressWarnings("unchecked")
    public <T extends IdObject> T read(final String input) {
        try {
            JsonNode rootNode = mapper.readTree(input);
            String entityTypeString = rootNode.get(JacksonIdObjectConstants.ENTITY_TYPE_FIELD).asText();
            Class entityType = Class.forName(entityTypeString);
            if (IdObject.class.isAssignableFrom(entityType)) {
                return mapper.readValue(input, (Class<T>) entityType);
            } else {
                throw new IllegalArgumentException("Unable to parse " + input + " as IdObject");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
