package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.vaadin.ui.Table;
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
public class PhotoTable extends IdObjectTable<Photo> {
//    @Autowired
//    private ClassListEditorDialogWindow classListEditorDialogWindow;

    public PhotoTable() {
        super(Photo.class);
    }

    private static final List<HeaderInfo> headers;

    static {
        headers = Arrays.asList(
                new HeaderInfo("description", "Description", Table.Align.LEFT),
                new HeaderInfo("modificationTimestamp", "Last Update", Table.Align.CENTER),
                new HeaderInfo("archived", "Archived?", Table.Align.CENTER),
                new HeaderInfo("actions", "Actions", Table.Align.RIGHT)    // Generated
        );
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    protected void showEntityEditor(final Photo entity) {
//        getUI().addWindow(classListEditorDialogWindow);
//        classListEditorDialogWindow.setEntity(entity);
    }

    @Override
    public void setTableDriver(final IdObject tableDriver) {
        super.setTableDriver(tableDriver);
        //  TODO - anything?
//        if (tableDriver instanceof ClassList) {
//            readWriteDAO.getAllStudentsForClassList((ClassList) tableDriver);
//            refreshSizeAndSort();
//        }
    }
}
