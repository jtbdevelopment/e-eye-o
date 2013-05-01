package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ClassListEditorDialogWindow;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.converters.LastObservationTimestampStringConverter;
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
public class ClassListTable extends ObservableTable<ClassList> {
    @Autowired
    private ClassListEditorDialogWindow classListEditorDialogWindow;

    @Autowired
    private LastObservationTimestampStringConverter lastObservationTimestampStringConverter;

    public ClassListTable() {
        super(ClassList.class);
    }

    private static final List<HeaderInfo> headers;

    static {
        headers = new LinkedList<>(
                Arrays.asList(
                        new HeaderInfo("description", "Description", Table.Align.LEFT)
                ));
        headers.addAll(ObservableTable.headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    public ClassListEditorDialogWindow showEntityEditor(final ClassList entity) {
        getUI().addWindow(classListEditorDialogWindow);
        classListEditorDialogWindow.setEntity(entity);
        return classListEditorDialogWindow;
    }
}
