package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Date: 1/27/13
 * Time: 10:23 PM
 */
@Service
public class JacksonIdObjectModule extends SimpleModule {
    private final JacksonIdTypeResolver jacksonIdTypeResolver;

    @Autowired
    @SuppressWarnings("unchecked")
    public JacksonIdObjectModule(final JacksonIdObjectSerializer serializer, final JacksonIdObjectDeserializer deserializer, final JacksonIdTypeResolver jacksonIdTypeResolver, final IdObjectFactory idObjectFactory) {
        addSerializer(IdObject.class, serializer);
        for (Class<? extends IdObject> implClass : idObjectFactory.implementationsForInterfaces().values()) {
            addDeserializer(implClass, deserializer);
        }
        this.jacksonIdTypeResolver = jacksonIdTypeResolver;
    }

    @Override
    public void setupModule(final SetupContext context) {
        super.setupModule(context);
        context.addAbstractTypeResolver(jacksonIdTypeResolver);
    }
}
