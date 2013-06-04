package com.jtbdevelopment.e_eye_o.entities.impl.reflection;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
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
    public <T extends IdObject> Collection<Map.Entry<String, Class>> getAllGetters(final Class<T> entityType) {
        Function<PropertyDescriptor, Map.Entry<String, Class>> function = new Function<PropertyDescriptor, Map.Entry<String, Class>>() {
            @Nullable
            @Override
            public Map.Entry<String, Class> apply(@Nullable PropertyDescriptor property) {
                if (property == null) {
                    return null;
                }
                return new AbstractMap.SimpleEntry<>(property.getName(), (Class) property.getPropertyType());
            }
        };
        List<Map.Entry<String, Class>> propertyList = traverseInterfaces(entityType, function);

        Collections.sort(propertyList, new Comparator<Map.Entry<String, Class>>() {
            @Override
            public int compare(final Map.Entry<String, Class> o1, final Map.Entry<String, Class> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return propertyList;
    }

    @Override
    public <T extends IdObject> Map<String, IdObjectFieldPreferences> getAllFieldPreferences(final Class<T> entityType) {
        final Function<PropertyDescriptor, Map.Entry<String, IdObjectFieldPreferences>> function = new Function<PropertyDescriptor, Map.Entry<String, IdObjectFieldPreferences>>() {
            @Nullable
            @Override
            public Map.Entry<String, IdObjectFieldPreferences> apply(@Nullable PropertyDescriptor property) {
                if (property == null) {
                    return null;
                }
                Method read = property.getReadMethod();
                if (read == null) {
                    return null;
                }
                IdObjectFieldPreferences preferences = read.getAnnotation(IdObjectFieldPreferences.class);
                if (preferences != null) {
                    return new AbstractMap.SimpleEntry<>(property.getName(), preferences);
                }
                return null;
            }
        };

        Map<String, IdObjectFieldPreferences> fieldPreferencesMap = new HashMap<>();
        for (Map.Entry<String, IdObjectFieldPreferences> field : traverseInterfaces(entityType, function)) {
            fieldPreferencesMap.put(field.getKey(), field.getValue());
        }
        return fieldPreferencesMap;
    }

    private <T extends IdObject, F> List<F> traverseInterfaces(Class<T> entityType, Function<PropertyDescriptor, F> function) {
        Class idObjectInterface = getIdObjectInterfaceForClass(entityType);
        List<F> traversalResults = new LinkedList<>();
        while (idObjectInterface != null) {
            List<PropertyDescriptor> propertyDescriptors = new LinkedList<>();
            Collections.addAll(propertyDescriptors, PropertyUtils.getPropertyDescriptors(idObjectInterface));
            Collection<PropertyDescriptor> filteredProperties = Collections2.filter(propertyDescriptors, new Predicate<PropertyDescriptor>() {
                @Override
                public boolean apply(@Nullable PropertyDescriptor property) {
                    if (property == null) {
                        return false;
                    }
                    Method read = property.getReadMethod();
                    return read != null && !"class".equals(read.getName()) && !read.isAnnotationPresent(Transient.class) && !"getPassword".equals(read.getName());
                }
            });
            traversalResults.addAll(Collections2.transform(filteredProperties, function));
            final Class<?>[] interfaces = idObjectInterface.getInterfaces();
            if (interfaces != null && interfaces.length == 1) {
                idObjectInterface = interfaces[0];
            } else {
                idObjectInterface = null;
            }
        }
        return traversalResults;
    }
}
