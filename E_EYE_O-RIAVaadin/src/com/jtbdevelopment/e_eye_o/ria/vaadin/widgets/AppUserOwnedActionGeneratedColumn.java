package com.jtbdevelopment.e_eye_o.ria.vaadin.widgets;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Date: 3/16/13
 * Time: 6:42 PM
 */
public class AppUserOwnedActionGeneratedColumn<T extends AppUserOwnedObject> implements Table.ColumnGenerator {
    private static final String ACTIVATE = "Activate";
    private static final String ARCHIVE = "Archive";

    private final ReadWriteDAO readWriteDAO;
    private final EventBus eventBus;
    private final AllItemsBeanItemContainer<T> entities;

    public AppUserOwnedActionGeneratedColumn(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final AllItemsBeanItemContainer<T> entities) {
        this.readWriteDAO = readWriteDAO;
        this.eventBus = eventBus;
        this.entities = entities;
    }

    @Override
    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
        final GridLayout layout = new GridLayout(2, 1);
        final T entity = entities.getItem(itemId).getBean();
        final Button archiveAction = new Button("ERROR");
        if (entity.isArchived()) {
            archiveAction.setCaption(ACTIVATE);
        } else {
            archiveAction.setCaption(ARCHIVE);
        }
        archiveAction.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final String caption = event.getButton().getCaption();
                switch (caption) {
                    case ARCHIVE:
                        //  TODO - implement
                        Notification.show("TO DO:  Archive " + entity.getViewableDescription());
                        break;
                    case ACTIVATE:
                        //  TODO - implement
                        Notification.show("TO DO:  Unarchive " + entity.getViewableDescription());
                        break;
                    default:
                        Notification.show("Something went wrong - button label = " + caption, Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                ConfirmDialog.show(layout.getUI(), "Delete " + entity.getViewableDescription(), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            //  TODO - make delete return set of everything deleted so all can be published
                            readWriteDAO.delete(entity);
                            eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.DELETED, entity));
                        }
                    }
                });
            }
        });
        layout.addComponent(archiveAction, 0, 0);
        layout.addComponent(deleteButton, 1, 0);
        return layout;
    }
}
