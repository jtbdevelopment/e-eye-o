package com.jtbdevelopment.e_eye_o.entities.impl.reflection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.lang.reflect.Method;
import java.util.*;

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
    public <T extends IdObject> Collection<Method> getAllGetters(final Class<T> entityType) {

        List<Method> methods = new LinkedList<>();
        Class iface = getIdObjectInterfaceForClass(entityType);
        while (iface != null) {
            for (PropertyDescriptor property : PropertyUtils.getPropertyDescriptors(iface)) {
                methods.add(property.getReadMethod());
            }
            final Class<?>[] interfaces = iface.getInterfaces();
            if (interfaces != null && interfaces.length == 1) {
                iface = interfaces[0];
            } else {
                iface = null;
            }
        }

        Collections.sort(methods, new Comparator<Method>() {
            @Override
            public int compare(final Method o1, final Method o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return Collections2.filter(methods, new Predicate<Method>() {
            @Override
            public boolean apply(@Nullable final Method input) {
                return input != null && !input.isAnnotationPresent(Transient.class) && !"class".equals(input.getName());
            }
        });
    }
}
