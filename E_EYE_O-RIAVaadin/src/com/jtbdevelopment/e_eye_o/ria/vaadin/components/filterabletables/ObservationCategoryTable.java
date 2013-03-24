package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.ObservationCategoryEditorDialogWindow;
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
public class ObservationCategoryTable extends IdObjectTable<ObservationCategory> {
    @Autowired
    private ObservationCategoryEditorDialogWindow observationCategoryEditorDialogWindow;

    @Autowired
    public ObservationCategoryTable(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus) {
        super(ObservationCategory.class, readWriteDAO, idObjectFactory, eventBus);
    }

    private static final List<HeaderInfo> headers;

    static {
        //  TODO - add last observation date
        headers = Arrays.asList(
                new HeaderInfo("shortName", "Short Name", Table.Align.LEFT),
                new HeaderInfo("description", "Description", Table.Align.LEFT),
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT)    // Generated
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected void showEntityEditor(final ObservationCategory entity) {
        getUI().addWindow(observationCategoryEditorDialogWindow);
        observationCategoryEditorDialogWindow.setEntity(entity);
    }

    @Override
    public void setTableDriver(final IdObject tableDriver) {
        super.setTableDriver(tableDriver);
    }
}