package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ClassListEditorDialogWindow;
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
public class ClassListTable extends GeneratedIdObjectTable<ClassList> {
    @Autowired
    private ClassListEditorDialogWindow classListEditorDialogWindow;

    public ClassListTable() {
        super(ClassList.class);
    }

    @Override
    public ClassListEditorDialogWindow showEntityEditor(final ClassList entity) {
        getUI().addWindow(classListEditorDialogWindow);
        classListEditorDialogWindow.setEntity(entity);
        return classListEditorDialogWindow;
    }

    @Override
    protected List<String> getTableFields() {
        return Arrays.asList(entityType.getAnnotation(IdObjectEntitySettings.class).viewFieldOrder());
    }
}
