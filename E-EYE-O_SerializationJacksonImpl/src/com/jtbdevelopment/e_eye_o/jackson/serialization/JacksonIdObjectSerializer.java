package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.io.IOException;

/**
 * Date: 2/18/13
 * Time: 11:25 AM
 */
public interface JacksonIdObjectSerializer {
    void serialize(IdObject value, JsonGenerator generator) throws IOException;
}
