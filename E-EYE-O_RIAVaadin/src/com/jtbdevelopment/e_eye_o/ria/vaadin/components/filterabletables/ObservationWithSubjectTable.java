package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters.ObservationSubjectFilter;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Or;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 4:45 PM
 */
@Component
@Scope("prototype")
public class ObservationWithSubjectTable extends ObservationTable {

    @Override
    protected Container.Filter generateFilter(String searchFor) {
        return new Or(
                super.generateFilter(searchFor),
                new ObservationSubjectFilter(searchFor)
        );
    }

    @Override
    protected String getDefaultSortField(final String[] properties) {
        return "observationTimestamp";
    }

    @Override
    protected List<String> getTableFields() {
        final List<String> s = super.getTableFields();
        return new LinkedList<String>() {{
            add("observationSubject");
            addAll(s);
        }};
    }
}
