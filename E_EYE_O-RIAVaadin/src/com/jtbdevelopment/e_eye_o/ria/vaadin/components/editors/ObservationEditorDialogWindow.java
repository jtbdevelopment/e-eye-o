package com.jtbdevelopment.e_eye_o.ria.vaadin.components.editors;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Runo;

/**
 * Date: 3/16/13
 * Time: 6:36 PM
 */
public class ObservationEditorDialogWindow extends Window {
    public ObservationEditorDialogWindow(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final Observation observation) {
        setSizeUndefined();
        setModal(true);
        addStyleName(Runo.WINDOW_DIALOG);
        setWidth(90, Unit.EM);
        setHeight(25, Unit.EM);
        setContent(new ObservationEditorForm(readWriteDAO, eventBus, observation));
    }

}
