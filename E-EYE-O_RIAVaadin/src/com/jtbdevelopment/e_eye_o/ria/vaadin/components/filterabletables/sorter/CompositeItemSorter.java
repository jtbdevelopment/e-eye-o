package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.sorter;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ItemSorter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

import java.util.Locale;

/**
 * Date: 4/5/13
 * Time: 9:08 PM
 */
public class CompositeItemSorter<T extends AppUserOwnedObject> implements ItemSorter {
    private Object[] propertyIds;
    private Table.ColumnGenerator[] generators;
    private Converter<String, Object>[] converters;
    private boolean[] ascending;
    private Table entityTable;
    private BeanItemContainer<T> entities;
    private Locale locale = null;

    public CompositeItemSorter(final Table entityTable, final BeanItemContainer<T> entities) {
        this.entityTable = entityTable;
        this.entities = entities;
    }

    @Override
    public void setSortProperties(Container.Sortable container, Object[] propertyId, boolean[] ascending) {
        this.propertyIds = propertyId;
        this.ascending = ascending;
        generators = new Table.ColumnGenerator[propertyId.length];
        converters = new Converter[propertyId.length];
        for (int i = 0; i < propertyIds.length; ++i) {
            Object property = propertyIds[i];
            generators[i] = entityTable.getColumnGenerator(property);
            converters[i] = entityTable.getConverter(property);
        }
    }

    @Override
    public int compare(Object itemId1, Object itemId2) {
        if (locale == null) {
            locale = UI.getCurrent().getLocale();
        }
        for (int i = 0; i < propertyIds.length; ++i) {
            if (converters[i] != null) {
                Object object1 = entityTable.getContainerProperty(itemId1, propertyIds[i]).getValue();
                Object object2 = entityTable.getContainerProperty(itemId2, propertyIds[i]).getValue();
                String value1 = converters[i].convertToPresentation(object1, locale);
                String value2 = converters[i].convertToPresentation(object2, locale);
                if (value1 != null) {
                    if (value2 != null) {
                        int compare = value1.compareTo(value2);
                        if (compare != 0) {
                            return compare * (ascending[i] ? 1 : -1);
                        }
                    } else {
                        return ascending[i] ? 1 : -1;
                    }
                }
            }
            Object object1 = null, object2 = null;
            if (generators[i] != null) {
                T entity1 = entities.getItem(itemId1).getBean();
                T entity2 = entities.getItem(itemId2).getBean();
                object1 = generators[i].generateCell(entityTable, entity1, propertyIds[i]);
                object2 = generators[i].generateCell(entityTable, entity2, propertyIds[i]);
            }
            if ((object1 == null && object2 == null) ||
                    (!(object1 instanceof Comparable) && !(object2 instanceof Comparable))) {
                object1 = entityTable.getContainerProperty(itemId1, propertyIds[i]).getValue();
                object2 = entityTable.getContainerProperty(itemId2, propertyIds[i]).getValue();
            }
            if (object1 instanceof Comparable) {
                if (object2 instanceof Comparable) {
                    int compare = ((Comparable) object1).compareTo(object2);
                    if (compare != 0) {
                        return compare * (ascending[i] ? 1 : -1);
                    } else {
                        return ascending[i] ? 1 : -1;

                    }
                }
            }
            int compare = entities.getItem(itemId1).getBean().getId().compareTo(entities.getItem(itemId2).getBean().getId());
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }
}
