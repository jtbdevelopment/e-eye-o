package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.vaadin.ui.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationCategoryEditorDialogWindow extends IdObjectEditorDialogWindow<ObservationCategory> {
    private final TextField shortName = new TextField();

    public ObservationCategoryEditorDialogWindow() {
        super(ObservationCategory.class, 80, 15);
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return shortName;
    }

    @Override
    protected Layout buildEditorLayout() {
        //  TODO - lot of duplication here with tables
        HorizontalLayout editorRow = new HorizontalLayout();
        editorRow.setSpacing(true);

        editorRow.addComponent(new Label("Short Name:"));
        shortName.setWidth(10, Unit.EM);
        entityBeanFieldGroup.bind(shortName, "shortName");
        editorRow.addComponent(shortName);

        editorRow.addComponent(new Label("Description:"));
        final TextArea longDescription = new TextArea();
        longDescription.setRows(3);
        longDescription.setWidth(20, Unit.EM);
        entityBeanFieldGroup.bind(longDescription, "description");
        editorRow.addComponent(longDescription);

        return editorRow;
    }

}
