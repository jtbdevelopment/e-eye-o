package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LastObservationTimestampStringConverter;
import com.vaadin.ui.Table;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 5/1/13
 * Time: 7:05 AM
 */
public abstract class ObservableTable<T extends Observable> extends IdObjectTable<T> {
    @Autowired
    private LastObservationTimestampStringConverter lastObservationTimestampStringConverter;

    protected static final List<HeaderInfo> headers;

    static {
        headers = new LinkedList<>(
                Arrays.asList(
                        new HeaderInfo("lastObservationTimestamp", "Last Observation", Table.Align.CENTER)
                ));
        headers.addAll(IdObjectTable.headers);
    }

    public ObservableTable(Class<T> entityType) {
        super(entityType);
    }

    @Override
    protected void addColumnConverters() {
        super.addColumnConverters();
        entityTable.setConverter("lastObservationTimestamp", lastObservationTimestampStringConverter);
    }
}
