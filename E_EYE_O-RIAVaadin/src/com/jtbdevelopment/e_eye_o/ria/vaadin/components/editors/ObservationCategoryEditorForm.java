package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/17/13
 * Time: 6:17 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ObservationCategoryEditorForm extends IdObjectEditorForm<ObservationCategory> {

    private final TextField shortName = new TextField();

    public ObservationCategoryEditorForm() {
        super(ObservationCategory.class);
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return shortName;
    }

    @Override
    public void setEntity(final ObservationCategory entity) {
        super.setEntity(entity);
    }

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
