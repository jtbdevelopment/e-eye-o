package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.Observable;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 5/1/13
 * Time: 7:05 AM
 */
public abstract class ObservableTable<T extends Observable> extends GeneratedIdObjectTable<T> {
    public ObservableTable(Class<T> entityType) {
        super(entityType);
    }

    @Override
    protected List<String> getTableFields() {
        final List<String> s = super.getTableFields();
        return new LinkedList<String>() {{
            add("lastObservationTimestamp");
            addAll(s);
        }};
    }
}
