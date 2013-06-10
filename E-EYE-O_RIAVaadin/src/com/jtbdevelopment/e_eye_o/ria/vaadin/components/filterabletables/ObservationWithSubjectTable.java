package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.filters.ObservationSubjectFilter;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Or;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/17/13
 * Time: 4:45 PM
 */
@Component
@Scope("prototype")
public class ObservationWithSubjectTable extends GeneratedObservationTable {

    @Override
    protected Container.Filter generateFilter(String searchFor) {
        return new Or(
                super.generateFilter(searchFor),
                new ObservationSubjectFilter(searchFor)
        );
    }
}
