package com.jtbdevelopment.e_eye_o.entities.reflection;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;
import java.util.Map;

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
    <T extends IdObject> Collection<Map.Entry<String, Class>> getAllGetters(final Class<T> entityType);
}
