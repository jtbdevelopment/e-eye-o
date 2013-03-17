package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.StudentEditorDialog;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.vaadin.ui.Table;

import java.util.Arrays;
import java.util.List;

/**
 * Date: 3/17/13
 * Time: 1:58 PM
 */
public abstract class StudentTable extends IdObjectTable<Student> {
    public StudentTable(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus, final AppUser appUser, final AllItemsBeanItemContainer<Student> entities) {
        super(Student.class, readWriteDAO, idObjectFactory, eventBus, appUser, entities);
    }

    private static final List<HeaderInfo> headers;

    static {
        //  TODO - add last observation date
        headers = Arrays.asList(
                new HeaderInfo("firstName", "First Name", Table.Align.LEFT),
                new HeaderInfo("lastName", "Last Name", Table.Align.LEFT),
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT)
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected void showEntityEditor(final Student entity) {
        getUI().addWindow(new StudentEditorDialog(readWriteDAO, eventBus, entity));
    }
}
