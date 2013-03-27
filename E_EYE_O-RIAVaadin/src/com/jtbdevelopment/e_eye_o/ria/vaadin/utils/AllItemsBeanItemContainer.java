package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ItemSorter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/16/13
 * Time: 4:56 PM
 * <p/>
 * Because the built in container only lets you access the viewable size (after filters applied)
 * Not useful if you want to base size decisions on potential items.
 */
public class AllItemsBeanItemContainer<T extends IdObject> extends BeanItemContainer<T> {
    private final Collection<String> additionalSortableProperties;

    public AllItemsBeanItemContainer(final Class<? super T> type) throws IllegalArgumentException {
        super(type);
        this.additionalSortableProperties = null;
    }

    public AllItemsBeanItemContainer(final Class<? super T> type, final Collection<String> additionalSortableProperties) throws IllegalArgumentException {
        super(type);
        this.additionalSortableProperties = additionalSortableProperties;
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        List<Object> allProperties = new LinkedList<>();
        allProperties.addAll(super.getSortableContainerPropertyIds());
        if (additionalSortableProperties != null) {
            allProperties.addAll(additionalSortableProperties);
        }
        return allProperties;
    }

    public int getUnfilteredSize() {
        return getAllItemIds().size();
    }

    @Override
    public void setItemSorter(final ItemSorter itemSorter) {
        super.setItemSorter(itemSorter);
    }
}
