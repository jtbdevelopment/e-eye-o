package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.io.IOException;

/**
 * Date: 2/18/13
 * Time: 11:26 AM
 */
public interface JacksonIdObjectDeserializer<T extends IdObject> {
    @SuppressWarnings("unchecked")
    T deserialize(JsonParser parser) throws IOException;
}
