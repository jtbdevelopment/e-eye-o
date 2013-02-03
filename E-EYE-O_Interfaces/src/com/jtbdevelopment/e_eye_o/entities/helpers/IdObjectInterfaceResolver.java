package com.jtbdevelopment.e_eye_o.entities.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Date: 1/28/13
 * Time: 10:36 PM
 *
 * Given a concrete implementation of an IdObject returns the interface it implements
 */
public interface IdObjectInterfaceResolver {
    public static final String GET = "get";
    public static final String IS = "is";

    @Cacheable("idObjectInterface")
    <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType);

    @Cacheable("idObjectInterface")
    <T extends IdObject> Method getIsOrGetMethod(final Class<T> entityType, final String attribute);

    @Cacheable("idObjectInterface")
    <T extends IdObject> Method getSetMethod(final Class<T> entityType, final String attribute, final Class valueType);

    /**
     *  Returns sorted list of getters
     */
    @Cacheable("idObjectInterface")
    <T extends IdObject> List<Method> getAllGetters(final Class<T> entityType);
}
