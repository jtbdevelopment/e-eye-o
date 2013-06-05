package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.vaadin.data.util.BeanItemContainer;

import java.util.*;

/**
 * Date: 3/16/13
 * Time: 4:56 PM
 */
public class AllItemsBeanItemContainer<T extends IdObject> extends BeanItemContainer<T> {
    private final Set<String> additionalSortableProperties = new LinkedHashSet<>();

    public AllItemsBeanItemContainer(final Class<? super T> type) throws IllegalArgumentException {
        super(type);
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        List<Object> allProperties = new LinkedList<>();
        allProperties.addAll(super.getSortableContainerPropertyIds());
        allProperties.addAll(additionalSortableProperties);
        return allProperties;
    }

    public void addAdditionalSortableProperty(final String additionalSortableProperty) {
        additionalSortableProperties.add(additionalSortableProperty);
    }

}
