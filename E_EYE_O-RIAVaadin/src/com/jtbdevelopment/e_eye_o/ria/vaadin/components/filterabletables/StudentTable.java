package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.StudentEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.ConverterCollection;
import com.vaadin.ui.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 1:58 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StudentTable extends IdObjectTable<Student> {
    @Autowired
    private StudentEditorDialogWindow studentEditorDialogWindow;

    @Autowired
    public StudentTable(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final ConverterCollection converterCollection) {
        super(Student.class, readWriteDAO, idObjectFactory, eventBus, converterCollection);
    }

    private static final List<HeaderInfo> headers;

    static {
        //  TODO - add last observation date
        headers = Arrays.asList(
                new HeaderInfo("firstName", "First Name", Table.Align.LEFT),
                new HeaderInfo("lastName", "Last Name", Table.Align.LEFT),
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT, true)    // Generated
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected void showEntityEditor(final Student entity) {
        getUI().addWindow(studentEditorDialogWindow);
        studentEditorDialogWindow.setEntity(entity);
    }

    @Override
    public void setTableDriver(final IdObject tableDriver) {
        super.setTableDriver(tableDriver);
        if (tableDriver instanceof ClassList) {
            entities.addAll(readWriteDAO.getAllStudentsForClassList((ClassList) tableDriver));
            refreshSizeAndSort();
        }
    }
}
