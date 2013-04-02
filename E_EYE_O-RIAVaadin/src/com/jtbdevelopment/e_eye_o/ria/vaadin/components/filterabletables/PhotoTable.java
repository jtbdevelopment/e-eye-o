package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors.IdObjectEditorDialogWindow;
import com.vaadin.ui.Table;
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
public class PhotoTable extends IdObjectTable<Photo> {
//    @Autowired
//    private ClassListEditorDialogWindow classListEditorDialogWindow;

    public PhotoTable() {
        super(Photo.class);
    }

    private static final List<HeaderInfo> headers;

    static {
        headers = new LinkedList<>(
                Arrays.asList(
                        new HeaderInfo("description", "Description", Table.Align.LEFT)
                ));
        headers.addAll(IdObjectTable.headers);
    }

    @Override
    protected List<HeaderInfo> getHeaderInfo() {
        return headers;
    }

    @Override
    public IdObjectEditorDialogWindow<Photo> showEntityEditor(final Photo entity) {
//        getUI().addWindow(classListEditorDialogWindow);
//        classListEditorDialogWindow.setEntity(entity);
        return null;
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
