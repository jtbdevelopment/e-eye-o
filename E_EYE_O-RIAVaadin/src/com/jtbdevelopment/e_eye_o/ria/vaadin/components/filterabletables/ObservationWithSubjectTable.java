package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.AppUserOwnedObjectStringConverter;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters.ObservationSubjectFilter;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 4:45 PM
 */
@Component
@Scope("prototype")
public class ObservationWithSubjectTable extends ObservationTable {

    @Autowired
    AppUserOwnedObjectStringConverter appUserOwnedObjectStringConverter;

    private static final List<HeaderInfo> headers;

    static {
        headers = new LinkedList<>(Arrays.asList(new HeaderInfo("observationSubject", "Subject", Table.Align.LEFT, true)));
        headers.addAll(ObservationTable.headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected Container.Filter generateFilter(String searchFor) {
        return new Or(
                super.generateFilter(searchFor),
                new ObservationSubjectFilter(searchFor)
        );
    }

    @Override
    protected String getDefaultSortField(List<String> properties) {
        return "observationTimestamp";
    }

    @Override
    protected void addColumnConverters() {
        super.addColumnConverters();
        entityTable.setConverter("observationSubject", appUserOwnedObjectStringConverter);
    }


}
