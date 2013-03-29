package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * Date: 3/20/13
 * Time: 8:13 PM
 */
public abstract class IdObjectEditorDialogWindow<T extends AppUserOwnedObject> extends Window {
    protected IdObjectEditorForm<T> editorForm;

    public IdObjectEditorDialogWindow(final int width, final int height, final IdObjectEditorForm<T> editorForm) {
        this.editorForm = editorForm;
        setSizeUndefined();
        setModal(true);
        addStyleName(Runo.WINDOW_DIALOG);
        setWidth(width, Unit.EM);
        setHeight(height, Unit.EM);
        this.editorForm = editorForm;
        setContent(editorForm);
    }

    public void setEntity(final T entity) {
        editorForm.setEntity(entity);
        setComponentError(null);
    }
}
