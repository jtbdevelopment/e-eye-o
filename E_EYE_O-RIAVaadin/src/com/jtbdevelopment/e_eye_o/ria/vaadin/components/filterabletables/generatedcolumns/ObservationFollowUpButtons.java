package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationTable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Date: 3/31/13
 * Time: 7:50 AM
 */
public class ObservationFollowUpButtons implements Table.ColumnGenerator {
    private final ObservationTable observationTable;

    private final EventBus eventBus;

    private final BeanItemContainer<Observation> entities;

    private final ReadWriteDAO readWriteDAO;

    public ObservationFollowUpButtons(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final ObservationTable observationTable, final BeanItemContainer<Observation> entities) {
        this.observationTable = observationTable;
        this.eventBus = eventBus;
        this.entities = entities;
        this.readWriteDAO = readWriteDAO;
    }

    @Override
    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
        final Observation entity = entities.getItem(itemId).getBean();
        GridLayout layout = new GridLayout(2, 1);
        layout.setSizeUndefined();
        if (entity.getFollowUpObservation() != null) {
            Button showFollowUpButton = new Button("See Follow Up");
            showFollowUpButton.addStyleName(Runo.BUTTON_SMALL);
            showFollowUpButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    observationTable.showEntityEditor(readWriteDAO.get(Observation.class, entity.getFollowUpObservation().getId()));
                }
            });
            layout.addComponent(showFollowUpButton, 0, 0);
            Button breakLink = new Button("Break Link");
            breakLink.addStyleName(Runo.BUTTON_SMALL);
            breakLink.setDescription("This will NOT delete the follow up observation, just break the link between the two observations and reset the follow-up flag for this observation.");
            breakLink.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    ConfirmDialog.show(UI.getCurrent(), "Change " + entity.getFollowUpObservation().getSummaryDescription() + " to not be a follow-up for this observation?", new ConfirmDialog.Listener() {
                        @Override
                        public void onClose(final ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                entity.setFollowUpNeeded(true);
                                entity.setFollowUpObservation(null);
                                Observation updatedEntity = readWriteDAO.update(entity);
                                eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, updatedEntity));
                            }
                        }
                    });
                }
            });
            layout.addComponent(breakLink, 1, 0);
        } else {
            Button addFollowUp = new Button("New");
            addFollowUp.setDescription("This starts a new observation.");
            addFollowUp.addStyleName(Runo.BUTTON_SMALL);
            addFollowUp.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification.show("TODO:  add new observation and link", Notification.Type.HUMANIZED_MESSAGE);
//                            showEntityEditor(idObjectFactory.newObservation(appUser));
                    //  TODO - link created observation to this observation and set follow up to false and reminder to null.
                }
            });
            layout.addComponent(addFollowUp, 0, 0);
            Button linkFollowUp = new Button("Link");
            linkFollowUp.setDescription("This lets you pick an existing observation as the follow-up.");
            linkFollowUp.addStyleName(Runo.BUTTON_SMALL);
            linkFollowUp.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification.show("TODO:  link existing observation", Notification.Type.HUMANIZED_MESSAGE);
                    //  TODO - show observations for entity and let them pick one to link and set follow up to false and reminder to null.
                }
            });
            layout.addComponent(linkFollowUp, 1, 0);
        }
        return layout;
    }
}

