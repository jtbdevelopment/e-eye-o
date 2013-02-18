package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 1/26/13
 * Time: 10:35 PM
 * <p/>
 * The intent of this customization is to provide two features:
 * 1)  clearly identify entity type on serialization
 * 2)  automatic type detection on deserialization from 1)
 * 3)  only shallow serialize to other entities - if a photo refers to a student, do not serialize out all the student details, just the entity type and id
 * 4)  load referred shallow entities from dao / cache on deserialization
 * <p/>
 * TODO - now that this is working, we don't seem to be getting much value out of using mapper and may make more sense to use streaming or node api's directly
 */
@Service
public class JacksonJSONIdObjectSerializer implements JSONIdObjectSerializer {
    public static final String ENTITY_TYPE_FIELD = "entityType";
    public static final String ID_FIELD = "id";

    private final MappingJsonFactory jsonFactory;
    private final JacksonIdObjectSerializer jacksonIdObjectSerializer;
    private final JacksonIdObjectDeserializer jacksonIdObjectDeserializer;

    @Autowired
    public JacksonJSONIdObjectSerializer(final JacksonIdObjectSerializer jacksonIdObjectSerializer, final JacksonIdObjectDeserializer jacksonIdObjectDeserializer) {
        jsonFactory = new MappingJsonFactory();
        jsonFactory.getCodec().registerModule(new JodaModule());
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
    public String write(final IdObject entity) {
        try (final JsonGenerator generator = createGenerator()) {
            jacksonIdObjectSerializer.serialize(entity, generator);
            return completeGeneration(generator);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String write(final Collection<? extends IdObject> entities) {
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

    @SuppressWarnings("unchecked")
    public <T> T read(final String input) {
        try (final JsonParser parser = jsonFactory.createJsonParser(input)) {
            JsonToken token = parser.nextToken();
            switch (token) {
                case START_OBJECT:
                    final T deserialized = (T) jacksonIdObjectDeserializer.deserialize(parser);
                    parser.close();
                    return deserialized;
                case START_ARRAY:
                    final List<IdObject> returnList = new LinkedList<>();
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        returnList.add(jacksonIdObjectDeserializer.deserialize(parser));
                    }
                    parser.close();
                    return (T) returnList;
                default:
                    throw new IllegalArgumentException("Invalid json input - does not start with array or object.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
