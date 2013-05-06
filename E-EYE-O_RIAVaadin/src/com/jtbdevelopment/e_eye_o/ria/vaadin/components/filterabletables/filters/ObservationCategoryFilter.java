package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.Set;

/**
 * Date: 3/30/13
 * Time: 8:27 PM
 */
public class ObservationCategoryFilter implements Container.Filter {
    private final String filterString;

    public ObservationCategoryFilter(final String filterString) {
        this.filterString = filterString.toLowerCase();
    }


    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
        Property property = item.getItemProperty("categories");
        if (property != null) {
            Set<ObservationCategory> categories = (Set<ObservationCategory>) property.getValue();
            for (ObservationCategory category : categories) {
                if (category.getShortName().toLowerCase().contains(filterString) || category.getDescription().toLowerCase().contains(filterString)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
        return propertyId.equals("categories");
    }
}
