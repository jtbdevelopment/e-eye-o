package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/17/13
 * Time: 6:17 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClassListEditorForm extends IdObjectEditorForm<ClassList> {

    private TextField description;

    @Autowired
    public ClassListEditorForm(final ReadWriteDAO readWriteDAO, final EventBus eventBus) {
        super(ClassList.class, readWriteDAO, eventBus);
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return description;
    }

    protected Layout buildEditorLayout() {
        //  TODO - lot of duplication here with tables
        HorizontalLayout editorRow = new HorizontalLayout();
        editorRow.setSpacing(true);

        editorRow.addComponent(new Label("Description:"));
        description = new TextField();
        entityBeanFieldGroup.bind(description, "description");
        editorRow.addComponent(description);

        return editorRow;
    }

}
