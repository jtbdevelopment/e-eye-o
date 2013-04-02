package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationCategoryEditorDialogWindow;
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
public class ObservationCategoryTable extends IdObjectTable<ObservationCategory> {
    @Autowired
    private ObservationCategoryEditorDialogWindow observationCategoryEditorDialogWindow;

    public ObservationCategoryTable() {
        super(ObservationCategory.class);
    }

    private static final List<HeaderInfo> headers;

    static {
        headers = new LinkedList<>(
                Arrays.asList(
                        new HeaderInfo("shortName", "Short Name", Table.Align.LEFT),
                        new HeaderInfo("description", "Description", Table.Align.LEFT)
                ));
        headers.addAll(IdObjectTable.headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    public ObservationCategoryEditorDialogWindow showEntityEditor(final ObservationCategory entity) {
        getUI().addWindow(observationCategoryEditorDialogWindow);
        observationCategoryEditorDialogWindow.setEntity(entity);
        return observationCategoryEditorDialogWindow;
    }
}
