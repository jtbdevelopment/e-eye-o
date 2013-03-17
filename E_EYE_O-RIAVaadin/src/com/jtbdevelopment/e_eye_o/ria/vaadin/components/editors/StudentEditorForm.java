package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.*;

/**
 * Date: 3/17/13
 * Time: 6:17 PM
 */
public class StudentEditorForm extends IdObjectEditorForm<Student> {

    private TextField firstName = new TextField();

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public StudentEditorForm(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final Student student) {
        super(Student.class, eventBus, readWriteDAO, student);
    }

    @Override
    protected Focusable getInitialFocusComponent() {
        return firstName;
    }

    protected Layout buildEditorLayout() {
        //  TODO - lot of duplication here with tables
        HorizontalLayout editorRow = new HorizontalLayout();
        editorRow.setSpacing(true);
        editorRow.addComponent(new Label("First Name:"));

        firstName = new TextField();
        beanFieldGroup.bind(firstName, "firstName");
        firstName.addValidator(new BeanValidator(Student.class, "firstName"));
        firstName.setImmediate(true);
        editorRow.addComponent(firstName);

        editorRow.addComponent(new Label("Last Name:"));
        final TextField lastName = new TextField();
        beanFieldGroup.bind(lastName, "lastName");
        lastName.addValidator(new BeanValidator(Student.class, "lastName"));
        lastName.setImmediate(true);
        editorRow.addComponent(lastName);

        editorRow.addComponent(new Label("Classes:"));
        BeanItemContainer<ClassList> potentialClasses = new BeanItemContainer<>(ClassList.class);
        if (entity.getArchivedClassLists().size() > 0) {
            potentialClasses.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, entity.getAppUser()));
        } else {
            potentialClasses.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, entity.getAppUser()));
        }

        final TwinColSelect classes = new TwinColSelect();
        classes.setRows(3);
        classes.setWidth(40, Unit.EM);
        classes.setContainerDataSource(potentialClasses);
        classes.setItemCaptionPropertyId("description");
        beanFieldGroup.bind(classes, "classLists");
        editorRow.addComponent(classes);
        return editorRow;
    }

}
