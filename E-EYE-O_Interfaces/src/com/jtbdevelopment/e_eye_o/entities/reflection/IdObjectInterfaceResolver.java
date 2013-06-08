package com.jtbdevelopment.e_eye_o.entities.reflection;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * Date: 1/28/13
 * Time: 10:36 PM
 * <p/>
 * Given a concrete implementation of an IdObject returns the interface it implements
 */
public interface IdObjectInterfaceResolver {
    @Cacheable("idObjectInterfaceForImpl")
    <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType);

    /**
     * Returns sorted list of getters
     */
    @Cacheable("idObjectGetterMethods")
    <T extends IdObject> Map<String, Method> getAllGetMethods(final Class<T> entityType);

    /**
     * Returns sorted list of getters
     */
    @Cacheable("idObjectGetMethodReturnTypes")
    <T extends IdObject> Collection<Map.Entry<String, Class>> getAllGetMethodReturns(final Class<T> entityType);


    @Cacheable("idObjectFieldPreferences")
    <T extends IdObject> Map<String, IdObjectFieldSettings> getAllFieldPreferences(final Class<T> entityType);
}
