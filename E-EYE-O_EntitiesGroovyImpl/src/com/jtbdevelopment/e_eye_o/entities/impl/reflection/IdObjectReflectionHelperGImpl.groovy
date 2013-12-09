package com.jtbdevelopment.e_eye_o.entities.impl.reflection

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper
import org.codehaus.groovy.reflection.CachedMethod
import org.springframework.stereotype.Service

import java.beans.Transient
import java.lang.reflect.Method

/**
 * Date: 12/3/13
 * Time: 7:08 PM
 */

@Service
public class IdObjectReflectionHelperGImpl implements IdObjectReflectionHelper {
    @Override
    def <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType) {
        if (entityType.interface) {
            return IdObject.isAssignableFrom(entityType) ? entityType : null
        }
        return (Class<T>) entityType.interfaces.find { i -> IdObject.isAssignableFrom(i) }
    }

    @Override
    def <T extends IdObject> Map<String, Method> getAllGetMethods(final Class<T> entityType) {
        Closure<Map<String, Method>> getGetter = {
            p ->
                [(p.name): ((CachedMethod) ((MetaBeanProperty) p).getGetter())?.getCachedMethod()]
        }
        return traverseInterfaces(entityType, getGetter)
    }

    @Override
    def <T extends IdObject> Map<String, Method> getAllSetMethods(final Class<T> entityType) {
        Closure<Map<String, Method>> getSetter = {
            p ->
                [(p.name): ((CachedMethod) ((MetaBeanProperty) p).getSetter())?.getCachedMethod()]
        }
        return traverseInterfaces(entityType, getSetter)
    }

    @Override
    def <T extends IdObject> Map<String, IdObjectFieldSettings> getAllFieldPreferences(final Class<T> entityType) {
        Map<String, Method> gets = getAllGetMethods(entityType)
        def map = [:]
        gets.each({ key, value ->
            map += [(key): value.getAnnotation(IdObjectFieldSettings.class)]
        })
        return map;
    }

    private <T extends IdObject> Map<String, Method> traverseInterfaces(final Class<T> entityType, final Closure<Map<String, ?>> function) {
        Class<T> i = getIdObjectInterfaceForClass(entityType)
        def map = [:] as Map<String, Method>
        while (i != null) {
            List<MetaProperty> ps = i.metaClass.properties
            ps.collect(function).each { map += it }
            i = (Class<T>) i.interfaces?.length == 1 ? i.interfaces[0] : null
        }
        Map<String, Method> all = map.findAll({ key, value -> value != null }).findAll { entry ->
            !entry.value.isAnnotationPresent(Transient.class)
        }
        return all
    }

}

