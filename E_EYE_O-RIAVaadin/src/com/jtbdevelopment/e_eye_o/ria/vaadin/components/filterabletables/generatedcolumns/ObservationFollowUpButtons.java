package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.ObservationTable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Runo;

import java.util.List;

/**
 * Date: 3/31/13
 * Time: 7:50 AM
 */
public class ObservationFollowUpButtons implements Table.ColumnGenerator {
    private final ObservationTable observationTable;

    private final EventBus eventBus;

    private final BeanItemContainer<Observation> entities;

    private final ReadWriteDAO readWriteDAO;

    private final IdObjectFactory idObjectFactory;

    public ObservationFollowUpButtons(final IdObjectFactory idObjectFactory, final ReadWriteDAO readWriteDAO, final EventBus eventBus, final ObservationTable observationTable, final BeanItemContainer<Observation> entities) {
        this.observationTable = observationTable;
        this.eventBus = eventBus;
        this.entities = entities;
        this.readWriteDAO = readWriteDAO;
        this.idObjectFactory = idObjectFactory;
    }

    @Override
    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
        final Observation entity = entities.getItem(itemId).getBean();
        GridLayout layout = new GridLayout(2, 1);
        layout.setSizeUndefined();
        if (readWriteDAO.getAllObservationFollowups(entity).isEmpty()) {
            Button addFollowUp = new Button("New");
            addFollowUp.setDescription("This starts a new observation.");
            addFollowUp.addStyleName(Runo.BUTTON_SMALL);
            addFollowUp.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    observationTable.showEntityEditor(idObjectFactory.newObservation(entity.getAppUser())).setFollowUpFor(entity);
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


        } else {
            Button showFollowUpButton = new Button("See Follow Ups");
            showFollowUpButton.addStyleName(Runo.BUTTON_SMALL);
            showFollowUpButton.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //  Might have changed since initial check
                    final List<Observation> allObservationFollowups = readWriteDAO.getAllObservationFollowups(entity);
                    if (allObservationFollowups.size() == 1) {
                        observationTable.showEntityEditor(allObservationFollowups.get(0));
                    } else {
                        //  TODO - picker
                        Notification.show("TODO - More than one linked followup");
                    }
                }
            });
            layout.addComponent(showFollowUpButton, 0, 0);
        }
        return layout;
    }
}

