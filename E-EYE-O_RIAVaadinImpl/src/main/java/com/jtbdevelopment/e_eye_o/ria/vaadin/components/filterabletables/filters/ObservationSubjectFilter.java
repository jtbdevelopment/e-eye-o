package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * Date: 3/30/13
 * Time: 8:01 PM
 */
public class ObservationSubjectFilter implements Container.Filter {
    private final String filterString;

    public ObservationSubjectFilter(final String filterString) {
        this.filterString = filterString.toLowerCase();
    }

    @Override
    public boolean passesFilter(final Object itemId, final Item item) throws UnsupportedOperationException {
        Property property = item.getItemProperty("observationSubject");
        return property != null && ((Observable) property.getValue()).getSummaryDescription().toLowerCase().contains(filterString);
    }

    @Override
    public boolean appliesToProperty(final Object propertyId) {
        return "summaryDescription".equals(propertyId) || "observationSubject".equals(propertyId) || "observationSubject.summaryDescription".equals(propertyId);
    }
}
