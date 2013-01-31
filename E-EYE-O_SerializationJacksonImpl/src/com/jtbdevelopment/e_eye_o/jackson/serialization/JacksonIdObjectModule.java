package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 1/27/13
 * Time: 10:23 PM
 */
@Service
public class JacksonIdObjectModule extends SimpleModule {

    @Autowired
    public JacksonIdObjectModule(final JacksonIdObjectSerializer serializer) {
        addSerializer(IdObject.class, serializer);
    }

    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
    }
}
