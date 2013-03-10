package com.jtbdevelopment.e_eye_o.entities.impl.reflection;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectInterfaceResolver;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

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
    public <T extends IdObject> Collection<Map.Entry<String, Class>> getAllGetters(final Class<T> entityType) {
        Map<String, Class> properties = new HashMap<>(20);
        Class idObjectInterface = getIdObjectInterfaceForClass(entityType);
        while (idObjectInterface != null) {
            for (PropertyDescriptor property : PropertyUtils.getPropertyDescriptors(idObjectInterface)) {
                Method read = property.getReadMethod();
                if (read != null && !"class".equals(read.getName()) && !read.isAnnotationPresent(Transient.class)) {
                    properties.put(property.getName(), property.getPropertyType());
                }
            }
            final Class<?>[] interfaces = idObjectInterface.getInterfaces();
            if (interfaces != null && interfaces.length == 1) {
                idObjectInterface = interfaces[0];
            } else {
                idObjectInterface = null;
            }
        }

        List<Map.Entry<String, Class>> properyList = new LinkedList<>(properties.entrySet());
        Collections.sort(properyList, new Comparator<Map.Entry<String, Class>>() {
            @Override
            public int compare(final Map.Entry<String, Class> o1, final Map.Entry<String, Class> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return properyList;
    }
}
