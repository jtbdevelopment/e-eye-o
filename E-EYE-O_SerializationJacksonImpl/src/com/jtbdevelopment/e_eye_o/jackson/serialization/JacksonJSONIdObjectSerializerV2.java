package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.PaginatedIdObjectList;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private final IdObjectFactory idObjectFactory;

    @Autowired
    public JacksonJSONIdObjectSerializerV2(final JacksonIdObjectSerializer jacksonIdObjectSerializer, final JacksonIdObjectDeserializerV2 jacksonIdObjectDeserializer, final IdObjectFactory idObjectFactory) {
        jsonFactory = new MappingJsonFactory();
        jsonFactory.getCodec().registerModule(new JodaModule());
        mapper = new ObjectMapper();
        this.jacksonIdObjectSerializer = jacksonIdObjectSerializer;
        this.jacksonIdObjectDeserializer = jacksonIdObjectDeserializer;
        this.idObjectFactory = idObjectFactory;
    }

    private JsonGenerator createGenerator() throws IOException {
        return jsonFactory.createGenerator(new StringWriter()).setPrettyPrinter(new DefaultPrettyPrinter());
    }

    private String completeGeneration(final JsonGenerator generator) throws IOException {
        generator.close();
        return generator.getOutputTarget().toString();
    }

    @Override
    public String write(final Object entity) {
        try (final JsonGenerator generator = createGenerator()) {
            if (entity instanceof IdObject) {
                jacksonIdObjectSerializer.serialize((IdObject) entity, generator);
            } else if (entity instanceof PaginatedIdObjectList) {
                writePaginatedIdObjectList(generator, (PaginatedIdObjectList) entity);
            } else if (entity instanceof Collection) {
                writeListOfIdObjects(generator, (Collection) entity);
            } else {
                throw new IllegalArgumentException("Don't know how to handle entity of type " + entity.getClass().getCanonicalName());
            }
            return completeGeneration(generator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writePaginatedIdObjectList(final JsonGenerator generator, final PaginatedIdObjectList paginatedIdObjectList) throws IOException {
        generator.writeStartObject();
        generator.writeBooleanField(MORE_FIELD, paginatedIdObjectList.isMoreAvailable());
        generator.writeNumberField(PAGE_SIZE, paginatedIdObjectList.getPageSize());
        generator.writeNumberField(CURRENT_PAGE, paginatedIdObjectList.getCurrentPage());
        writeListOfIdObjects(generator, paginatedIdObjectList.getEntities());
        generator.writeEndObject();
    }

    private void writeListOfIdObjects(final JsonGenerator generator, final Collection entities) throws IOException {
        generator.writeStartArray();
        for (Object entity : entities) {
            if (entity instanceof IdObject) {
                jacksonIdObjectSerializer.serialize((IdObject) entity, generator);
            } else {
                throw new IllegalArgumentException("Can only write IdObject items in collection not " + entity.getClass().getCanonicalName());
            }
        }
        generator.writeEndArray();
    }

    private boolean isPaginatedList(final Map<String, Object> valueMap) {
        return valueMap.containsKey(MORE_FIELD) && valueMap.containsKey(ENTITIES_FIELD);
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
    public <T> T readRaw(final String input) {
        try {
            return (T) mapper.readValue(input, Object.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T readAsObjects(final String input) {
        try {
            Object values = readRaw(input);
            if (values instanceof Map) {
                Map<String, Object> valueMap = (Map<String, Object>) values;
                if (isPaginatedList(valueMap)) {
                    return (T) readPaginatedIdObjectList(valueMap);
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

    private PaginatedIdObjectList readPaginatedIdObjectList(final Map<String, Object> valueMap) throws IOException {
        PaginatedIdObjectList paginatedIdObjectList = idObjectFactory.newPaginatedIdObjectList();
        paginatedIdObjectList.setMoreAvailable((Boolean) valueMap.get(MORE_FIELD));
        paginatedIdObjectList.setEntities(readList((List<Map<String, Object>>) valueMap.get(ENTITIES_FIELD)));
        if (valueMap.containsKey(PAGE_SIZE)) {
            paginatedIdObjectList.setPageSize((Integer) valueMap.get(PAGE_SIZE));
        }
        if (valueMap.containsKey(CURRENT_PAGE)) {
            paginatedIdObjectList.setCurrentPage((Integer) valueMap.get(CURRENT_PAGE));
        }
        return paginatedIdObjectList;
    }
}
