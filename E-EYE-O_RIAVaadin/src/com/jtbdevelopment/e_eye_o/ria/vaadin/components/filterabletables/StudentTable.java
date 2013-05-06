package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.StudentEditorDialogWindow;
import com.vaadin.ui.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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

    private static final List<HeaderInfo> headers;

    static {
        headers = new LinkedList<>(
                Arrays.asList(
                        new HeaderInfo("firstName", "First Name", Table.Align.LEFT),
                        new HeaderInfo("lastName", "Last Name", Table.Align.LEFT)
                ));
        headers.addAll(ObservableTable.headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
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

}
