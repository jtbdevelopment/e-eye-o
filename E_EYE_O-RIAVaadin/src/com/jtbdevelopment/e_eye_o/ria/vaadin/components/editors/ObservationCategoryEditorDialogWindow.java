package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
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
        //  height seems to be ignored as less than some minimum it wants
        super(ObservationCategory.class, 50, 2);
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
        shortName.setWidth(5, Unit.EM);
        entityBeanFieldGroup.bind(shortName, "shortName");
        editorRow.addComponent(shortName);

        editorRow.addComponent(new Label("Description:"));
        final TextField longDescription = new TextField();
        longDescription.setWidth(30, Unit.EM);
        entityBeanFieldGroup.bind(longDescription, "description");
        editorRow.addComponent(longDescription);

        return editorRow;
    }

}
