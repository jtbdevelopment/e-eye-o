package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 2:02 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationWithoutSubjectTable extends GeneratedObservationTable {

    @Override
    protected List<String> getTableFields() {
        return new LinkedList<>(Collections2.filter(super.getTableFields(), new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return input != null && !"observationSubject".equals(input);
            }
        }));
    }
}
