package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudentEditorDialogWindow extends GeneratedEditorDialogWindow<Student> {
    private final BeanItemContainer<ClassList> potentialClasses = new BeanItemContainer<>(ClassList.class);

    public StudentEditorDialogWindow() {
        super(Student.class, 50, 15.5f);
    }

    @Override
    protected void addDataSourceToSelectField(final String fieldName, final AbstractSelect select) {
        switch (fieldName) {
            case "classLists":
                select.setContainerDataSource(potentialClasses);
                select.setItemCaptionPropertyId("description");
                break;
        }
        super.addDataSourceToSelectField(fieldName, select);
    }

    @Override
    public void setEntity(final Student student) {
        potentialClasses.removeAllItems();
        if (student.getArchivedClassLists().size() > 0) {
            potentialClasses.addAll(readWriteDAO.getEntitiesForUser(ClassList.class, student.getAppUser()));
        } else {
            potentialClasses.addAll(readWriteDAO.getActiveEntitiesForUser(ClassList.class, student.getAppUser()));
        }
        potentialClasses.sort(new String[]{"description"}, new boolean[]{true});
        super.setEntity(student);
    }
}
