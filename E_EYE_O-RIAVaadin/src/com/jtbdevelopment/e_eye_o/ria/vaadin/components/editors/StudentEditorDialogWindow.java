package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudentEditorDialogWindow extends IdObjectEditorDialogWindow<Student> {
    private final TextField firstName = new TextField();
    private final BeanItemContainer<ClassList> potentialClasses = new BeanItemContainer<>(ClassList.class);

    public StudentEditorDialogWindow() {
        super(Student.class, 80, 15);
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return firstName;
    }

    @Override
    public void setEntity(final Student student) {
        potentialClasses.removeAllItems();
        if (student.getArchivedClassLists().size() > 0) {
            potentialClasses.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, student.getAppUser()));
        } else {
            potentialClasses.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, student.getAppUser()));
        }
        super.setEntity(student);
    }

    @Override
    protected Layout buildEditorLayout() {
        //  TODO - lot of duplication here with tables
        HorizontalLayout editorRow = new HorizontalLayout();
        editorRow.setSpacing(true);

        editorRow.addComponent(new Label("First Name:"));
        entityBeanFieldGroup.bind(firstName, "firstName");
        editorRow.addComponent(firstName);

        editorRow.addComponent(new Label("Last Name:"));
        final TextField lastName = new TextField();
        entityBeanFieldGroup.bind(lastName, "lastName");
        editorRow.addComponent(lastName);

        editorRow.addComponent(new Label("Classes:"));

        TwinColSelect classes = new TwinColSelect();
        classes.setRows(3);
        classes.setContainerDataSource(potentialClasses);
        classes.setItemCaptionPropertyId("description");
        entityBeanFieldGroup.bind(classes, "classLists");
        editorRow.addComponent(classes);
        return editorRow;
    }
}
