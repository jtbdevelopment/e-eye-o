package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.Semester;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.SemesterEditorDialogWindow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 11/12/13
 * Time: 9:36 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SemesterTable extends GeneratedIdObjectTable<Semester> {
    @Autowired
    private SemesterEditorDialogWindow semesterEditorDialogWindow;

    public SemesterTable() {
        super(Semester.class);
    }

    @Override
    public SemesterEditorDialogWindow showEntityEditor(final Semester entity) {
        getUI().addWindow(semesterEditorDialogWindow);
        semesterEditorDialogWindow.setEntity(entity);
        return semesterEditorDialogWindow;
    }
}
