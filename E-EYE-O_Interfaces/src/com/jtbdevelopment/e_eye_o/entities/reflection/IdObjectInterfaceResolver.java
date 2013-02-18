package com.jtbdevelopment.e_eye_o.entities.reflection;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Date: 1/28/13
 * Time: 10:36 PM
 * <p/>
 * Given a concrete implementation of an IdObject returns the interface it implements
 */
public interface IdObjectInterfaceResolver {
    public static final String GET = "get";
    public static final String IS = "is";

    @Cacheable("idObjectInterfaceForImpl")
    <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType);

    @Cacheable("idObjectGetMethod")
    <T extends IdObject> Method getIsOrGetMethod(final Class<T> entityType, final String attribute);

    @Cacheable("idObjectSetMethod")
    <T extends IdObject> Method getSetMethod(final Class<T> entityType, final String attribute, final Class valueType);

    /**
     * Returns sorted list of getters
     */
    @Cacheable("idObjectGetMethods")
    <T extends IdObject> List<Method> getAllGetters(final Class<T> entityType);
}
