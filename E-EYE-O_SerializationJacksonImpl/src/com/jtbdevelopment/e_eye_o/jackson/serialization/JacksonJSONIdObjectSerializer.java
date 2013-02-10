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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 1/26/13
 * Time: 10:35 PM
 *
 * The intent of this customization is to provide two features:
 *  1)  clearly identify entity type on serialization
 *  2)  automatic type detection on deserialization from 1)
 *  3)  only shallow serialize to other entities - if a photo refers to a student, do not serialize out all the student details, just the entity type and id
 *  4)  load referred shallow entities from dao / cache on deserialization
 *
 * TODO - now that this is working, we don't seem to be getting much value out of using mapper and may make more sense to use streaming or node api's directly
 *
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
    public String write(final IdObject entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String write(final Collection<? extends IdObject> entities) {
        try {
            return mapper.writeValueAsString(entities);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T read(final String input) {
        try {
            JsonNode rootNode = mapper.readTree(input);
            if (rootNode.isArray()) {
                List<IdObject> returnList = new LinkedList<>();
                for(JsonNode child : rootNode) {
                    returnList.add((IdObject) read(child.toString()));
                }
                return (T) returnList;
            } else {
                return readSingleObject(input, rootNode);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T readSingleObject(final String input, final JsonNode rootNode) throws ClassNotFoundException, IOException {
        String entityTypeString = rootNode.get(JacksonIdObjectConstants.ENTITY_TYPE_FIELD).asText();
        Class entityType = Class.forName(entityTypeString);
        if (IdObject.class.isAssignableFrom(entityType)) {
            return mapper.readValue(input, (Class<T>) entityType);
        } else {
            throw new IllegalArgumentException("Unable to parse " + input + " as IdObject");
        }
    }
}
