package com.jtbdevelopment.e_eye_o.ria.vaadin.utils;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.vaadin.data.util.BeanItemContainer;

import java.util.Collection;

/**
 * Date: 3/16/13
 * Time: 4:56 PM
 * <p/>
 * Because the built in container only lets you access the viewable size (after filters applied)
 * Not useful if you want to base size decisions on potential items.
 */
public class AllItemsBeanItemContainer<T extends IdObject> extends BeanItemContainer<T> {

    public AllItemsBeanItemContainer(final Class<? super T> type) throws IllegalArgumentException {
        super(type);
    }

    @Deprecated
    public AllItemsBeanItemContainer(final Collection<? extends T> collection) throws IllegalArgumentException {
        super(collection);
    }

    public AllItemsBeanItemContainer(final Class<? super T> type, final Collection<? extends T> collection) throws IllegalArgumentException {
        super(type, collection);
    }

    public int getUnfilteredSize() {
        return getAllItemIds().size();
    }
}
