package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.StudentEditorDialogWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 1:58 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudentTable extends ObservableTable<Student> {
    @Autowired
    private StudentEditorDialogWindow studentEditorDialogWindow;

    public StudentTable() {
        super(Student.class);
    }

    @Override
    public StudentEditorDialogWindow showEntityEditor(final Student entity) {
        getUI().addWindow(studentEditorDialogWindow);
        studentEditorDialogWindow.setEntity(entity);
        return studentEditorDialogWindow;
    }

    @Override
    public void setDisplayDriver(final IdObject tableDriver) {
        super.setDisplayDriver(tableDriver);
        if (tableDriver instanceof ClassList) {
            entities.addAll(readWriteDAO.getAllStudentsForClassList((ClassList) tableDriver));
            refreshSizeAndSort();
        }
    }

    @Override
    protected List<String> getTableFields() {
        final List<String> s = super.getTableFields();
        return new LinkedList<String>() {{
            add("firstName");
            add("lastName");
            addAll(s);
        }};
    }
}
