package com.jtbdevelopment.e_eye_o.entities.impl.helpers;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.helpers.IdObjectInterfaceResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 1/28/13
 * Time: 10:39 PM
 */
@Service
@SuppressWarnings("unused")
public class IdObjectInterfaceResolverImpl implements IdObjectInterfaceResolver {

    @SuppressWarnings("unchecked")
    public <T extends IdObject> Class<T> getIdObjectInterfaceForClass(final Class<T> entityType) {
        if(entityType.isInterface())
            return entityType;

        Class<T> found = null;
        for (Class i : entityType.getInterfaces()) {
            if (IdObject.class.isAssignableFrom(i)) {
                found = (Class<T>) i;
                break;
            }
        }
        return found;
    }


    @Override
    public <T extends IdObject> Method getSetMethod(final Class<T> entityType, final String attribute, final Class valueType) {
        try {
            return entityType.getMethod("set" + StringUtils.capitalize(attribute), valueType);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends IdObject> Method getIsOrGetMethod(final Class<T> entityType, final String attribute) {
        try {
            return entityType.getMethod("is" + StringUtils.capitalize(attribute));
        } catch (NoSuchMethodException e) {
            //
        }
        return getGetMethod(entityType, attribute);
    }

    protected <T extends IdObject> Method getGetMethod(final Class<T> entityType, final String attribute) {
        try {
            return entityType.getMethod("get" + StringUtils.capitalize(attribute));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends IdObject> List<Method> getAllGetters(final Class<T> entityType) {

        List<Method> methods = new LinkedList<>();
        Class<T> iface = getIdObjectInterfaceForClass(entityType);
        for (Method method : iface.getMethods()) {
            String methodName = method.getName();
            if (method.isAnnotationPresent(Transient.class)) {
                continue;
            }

            if (method.getName().startsWith(GET) || method.getName().startsWith(IS)) {
                methods.add(method);
            }

        }
        return methods;
    }
}
