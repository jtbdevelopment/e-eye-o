package com.jtbdevelopment.e_eye_o.jackson.serialization;

import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JacksonIdTypeResolver extends AbstractTypeResolver {
    final IdObjectFactory idObjectFactory;

    @Autowired
    public JacksonIdTypeResolver(final IdObjectFactory idObjectFactory) {
        this.idObjectFactory = idObjectFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JavaType findTypeMapping(final DeserializationConfig config, final JavaType type) {
        final Class<?> rawClass = type.getRawClass();
        if (rawClass.isInterface() && IdObject.class.isAssignableFrom(rawClass)) {
            Class<IdObject> impl = idObjectFactory.implementationForInterface((Class<IdObject>) rawClass);
            return config.constructType(impl);
        }
        return super.findTypeMapping(config, type);
    }

    //  TODO - determine if ever called
    @Override
    @SuppressWarnings("unchecked")
    public JavaType resolveAbstractType(final DeserializationConfig config, final JavaType type) {
        final Class<?> rawClass = type.getRawClass();
        if (rawClass.isInterface() && IdObject.class.isAssignableFrom(rawClass)) {
            Class<IdObject> impl = idObjectFactory.implementationForInterface((Class<IdObject>) rawClass);
            return config.constructType(impl);
        }
        return super.resolveAbstractType(config, type);
    }
}