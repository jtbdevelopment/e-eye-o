package com.jtbdevelopment.e_eye_o.entities.impl.reflection;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
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
        if (entityType.isInterface())
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
            if (AppUserOwnedObject.class.isAssignableFrom(valueType)) {
                try {
                    return entityType.getMethod("set" + StringUtils.capitalize(attribute), AppUserOwnedObject.class);
                } catch (NoSuchMethodException e2) {
                    throw new RuntimeException(e2);
                }
            }
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
            if (method.isAnnotationPresent(Transient.class)) {
                continue;
            }

            String methodName = method.getName();
            if (methodName.startsWith(GET) || methodName.startsWith(IS)) {
                methods.add(method);
            }

        }
        return getSortedMethods(methods);
    }

    private List<Method> getSortedMethods(final List<Method> methods) {
        Collections.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(final Method o1, final Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return methods;
    }
}
