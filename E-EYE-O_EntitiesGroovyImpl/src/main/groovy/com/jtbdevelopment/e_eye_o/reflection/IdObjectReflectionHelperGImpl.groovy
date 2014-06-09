package com.jtbdevelopment.e_eye_o.reflection

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
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
    public <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType) {
        if (entityType.interface) {
            return IdObject.isAssignableFrom(entityType) ? entityType : null
        }
        return (Class<T>) entityType.interfaces.find { i -> IdObject.isAssignableFrom(i) }
    }

    @Override
    public Map<String, Method> getAllGetMethods(final Class<? extends IdObject> entityType) {
        Closure getGetter = {
            MetaProperty p ->
                [(p.name): ((CachedMethod) ((MetaBeanProperty) p).getGetter())?.getCachedMethod()]
        }
        return traverseInterfaces(entityType, getGetter)
    }

    @Override
    public Map<String, Method> getAllSetMethods(final Class<? extends IdObject> entityType) {
        Closure getSetter = {
            MetaProperty p ->
                [(p.name): ((CachedMethod) ((MetaBeanProperty) p).getSetter())?.getCachedMethod()]
        }
        return traverseInterfaces(entityType, getSetter)
    }

    @Override
    public Map<String, IdObjectFieldSettings> getAllFieldPreferences(final Class<? extends IdObject> entityType) {
        Map<String, Method> gets = getAllGetMethods(entityType)
        Map<String, IdObjectFieldSettings> map = (Map<String, IdObjectFieldSettings>) gets.collectEntries { key, value ->
            [(key): value.getAnnotation(IdObjectFieldSettings.class)]
        }
        return map.findAll { it.value != null };
    }

    private <T extends IdObject> Map<String, Method> traverseInterfaces(
            final Class<T> entityType, final Closure function) {
        Class<T> i = getIdObjectInterfaceForClass(entityType)
        def map = [:] as Map<String, Method>
        while (i != null) {
            List<MetaProperty> ps = i.metaClass.properties
            map += ps.collectEntries(function)
            i = (Class<T>) i.interfaces?.length == 1 ? i.interfaces[0] : null
        }
        Map<String, Method> all = map.findAll({ key, value -> value != null }).findAll { entry ->
            !entry.value.isAnnotationPresent(Transient.class) && "class" != entry.key
        }
        return all
    }

}


