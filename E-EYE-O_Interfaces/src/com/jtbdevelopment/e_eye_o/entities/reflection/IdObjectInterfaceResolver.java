package com.jtbdevelopment.e_eye_o.entities.reflection;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Method;
import java.util.Collection;

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

    /**
     * Returns sorted list of getters
     */
    @Cacheable("idObjectGetMethods")
    <T extends IdObject> Collection<Method> getAllGetters(final Class<T> entityType);
}
