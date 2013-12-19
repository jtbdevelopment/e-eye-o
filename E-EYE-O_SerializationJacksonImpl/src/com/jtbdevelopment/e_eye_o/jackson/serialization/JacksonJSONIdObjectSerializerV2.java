package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Date: 1/26/13
 * Time: 10:35 PM
 * <p/>
 * The intent of this customization is to provide four features:
 * 1)  clearly identify entity type on serialization
 * 2)  automatic type detection on deserialization from 1)
 * 3)  only shallow serialize to other entities - if a photo refers to a student, do not serialize out all the student details, just the entity type and id
 * 4)  load referred shallow entities from dao / cache on deserialization
 * <p/>
 */
@Service
public class JacksonJSONIdObjectSerializerV2 implements JSONIdObjectSerializer {
    private final MappingJsonFactory jsonFactory;
    private final JacksonIdObjectSerializer jacksonIdObjectSerializer;
    private final JacksonIdObjectDeserializerV2 jacksonIdObjectDeserializer;
    private final ObjectMapper mapper;

    @Autowired
    public JacksonJSONIdObjectSerializerV2(final JacksonIdObjectSerializer jacksonIdObjectSerializer, final JacksonIdObjectDeserializerV2 jacksonIdObjectDeserializer) {
        jsonFactory = new MappingJsonFactory();
        jsonFactory.getCodec().registerModule(new JodaModule());
        mapper = new ObjectMapper();
        this.jacksonIdObjectSerializer = jacksonIdObjectSerializer;
        this.jacksonIdObjectDeserializer = jacksonIdObjectDeserializer;
    }

    private JsonGenerator createGenerator() throws IOException {
        return jsonFactory.createGenerator(new StringWriter()).setPrettyPrinter(new DefaultPrettyPrinter());
    }

    private String completeGeneration(final JsonGenerator generator) throws IOException {
        generator.close();
        return generator.getOutputTarget().toString();
    }

    @Override
    public String writeEntity(final IdObject entity) {
        try (final JsonGenerator generator = createGenerator()) {
            jacksonIdObjectSerializer.serialize(entity, generator);
            return completeGeneration(generator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String writeEntities(final Collection<? extends IdObject> entities) {
        try (final JsonGenerator generator = createGenerator()) {
            generator.writeStartArray();
            for (IdObject entity : entities) {
                jacksonIdObjectSerializer.serialize(entity, generator);
            }
            generator.writeEndArray();
            return completeGeneration(generator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String writeMap(final Map<String, Object> map) {
        try (JsonGenerator generator = createGenerator()) {
            generator.writeStartObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                generator.writeFieldName(entry.getKey());
                Object value = entry.getValue();
                if (value instanceof Collection) {
                    generator.writeStartArray();
                    for (Object object : (Collection) value) {
                        if (object instanceof IdObject) {
                            jacksonIdObjectSerializer.serialize((IdObject) object, generator);
                        } else {
                            generator.writeObject(object);
                        }
                    }
                    generator.writeEndArray();
                } else {
                    generator.writeObject(value);
                }
            }
            generator.writeEndObject();
            return completeGeneration(generator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> readToMap(final String input) {
        try {
            return mapper.readValue(input,
                    new TypeReference<HashMap<String, Object>>() {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isPaginatedList(final Map<String, Object> valueMap) {
        return valueMap.size() == 2 && valueMap.containsKey(MORE_FIELD) && valueMap.containsKey(ENTITIES_FIELD);
    }

    private <T extends IdObject> T readMap(final Map<String, Object> valueMap) throws IOException {
        return jacksonIdObjectDeserializer.deserialize(valueMap);
    }

    private <T extends IdObject> List<T> readList(final List<Map<String, Object>> valueArray) throws IOException {
        final List<T> returnList = new LinkedList<>();
        for (Map<String, Object> valueMap : valueArray) {
            returnList.add((T) readMap(valueMap));
        }
        return returnList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readNoPOJO(final String input) {
        try {
            return (T) mapper.readValue(input, Object.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(final String input) {
        try {
            Object values = readNoPOJO(input);
            if (values instanceof Map) {
                Map<String, Object> valueMap = (Map<String, Object>) values;
                if (isPaginatedList(valueMap)) {
                    Map<String, Object> paginatedResult = new HashMap<>();
                    paginatedResult.put(MORE_FIELD, valueMap.get(MORE_FIELD));
                    paginatedResult.put(ENTITIES_FIELD, readList((List<Map<String, Object>>) valueMap.get(ENTITIES_FIELD)));
                    return (T) paginatedResult;
                } else {
                    return (T) readMap(valueMap);
                }
            } else if (values instanceof List) {
                return (T) readList((List<Map<String, Object>>) values);

            } else {
                throw new RuntimeException("readValue produced non-list/non-map object " + values.getClass().getName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
