package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

import java.io.IOException;
import java.util.Map;

/**
 * Date: 2/18/13
 * Time: 11:26 AM
 */
public interface JacksonIdObjectDeserializerV2 {
    @SuppressWarnings("unchecked")
    <T extends IdObject> T deserialize(final Map<String, Object> values) throws IOException;
}
