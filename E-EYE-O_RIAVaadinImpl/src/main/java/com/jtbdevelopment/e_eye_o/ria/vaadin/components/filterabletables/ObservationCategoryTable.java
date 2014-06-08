package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationCategoryEditorDialogWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/17/13
 * Time: 1:58 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationCategoryTable extends GeneratedIdObjectTable<ObservationCategory> {
    @Autowired
    private ObservationCategoryEditorDialogWindow observationCategoryEditorDialogWindow;

    public ObservationCategoryTable() {
        super(ObservationCategory.class);
    }

    @Override
    public ObservationCategoryEditorDialogWindow showEntityEditor(final ObservationCategory entity) {
        getUI().addWindow(observationCategoryEditorDialogWindow);
        observationCategoryEditorDialogWindow.setEntity(entity);
        return observationCategoryEditorDialogWindow;
    }
}
