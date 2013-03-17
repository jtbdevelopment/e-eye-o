package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
public class StudentEditorDialogWindow extends Window {
    public StudentEditorDialogWindow(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final Student student) {
        setSizeUndefined();
        setModal(true);
        addStyleName(Runo.WINDOW_DIALOG);
        setWidth(80, Unit.EM);
        setHeight(15, Unit.EM);
        setContent(new StudentEditorForm(readWriteDAO, eventBus, student));
    }

}
