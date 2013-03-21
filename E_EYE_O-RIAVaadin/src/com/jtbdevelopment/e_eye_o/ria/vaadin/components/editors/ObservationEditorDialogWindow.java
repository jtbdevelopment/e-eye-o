package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.Observation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationEditorDialogWindow extends IdObjectEditorDialogWindow<Observation> {
    @Autowired
    public ObservationEditorDialogWindow(final ObservationEditorForm observationEditorForm) {
        super(90, 25, observationEditorForm);
    }
}
