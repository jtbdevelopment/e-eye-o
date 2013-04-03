package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Table;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Date: 3/16/13
 * Time: 6:42 PM
 */
public class ArchiveAndDeleteButtons<T extends AppUserOwnedObject> implements Table.ColumnGenerator {
    private static final String ACTIVATE = "Activate";
    private static final String ARCHIVE = "Archive";
    //  TODO - diff icons
    private static final ThemeResource ARCHIVE_ICON = new ThemeResource("../runo/icons/16/lock.png");
    private static final ThemeResource ACTIVATE_ICON = new ThemeResource("../runo/icons/16/user.png");
    private static final ThemeResource DELETE_ICON = new ThemeResource("../runo/icons/16/trash.png");

    private final ReadWriteDAO readWriteDAO;
    private final EventBus eventBus;
    private final AllItemsBeanItemContainer<T> entities;

    public ArchiveAndDeleteButtons(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final AllItemsBeanItemContainer<T> entities) {
        this.readWriteDAO = readWriteDAO;
        this.eventBus = eventBus;
        this.entities = entities;
    }

    @Override
    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
        final GridLayout layout;
        layout = new GridLayout(2, 1);
        final T entity = entities.getItem(itemId).getBean();

        Embedded archiveAction;
        if (entity.isArchived()) {
            archiveAction = new Embedded(null, ACTIVATE_ICON);
        } else {
            archiveAction = new Embedded(null, ARCHIVE_ICON);
        }
        archiveAction.setDescription("Archiving anything does not delete it, just drops it off your lists by default.  You can always get to them and you can always re-activate them.  Archiving something will archive related items.");
        archiveAction.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                publishUpdatedDAOObjects(readWriteDAO.changeArchiveStatus(entity));
            }
        });
        /*
        final Button archiveAction = new Button("ERROR");
        archiveAction.setDescription("Archiving anything does not delete it, just drops it off your lists by default.  You can always get to them and you can always re-activate them.  Archiving something will archive related items.");
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
                    case ACTIVATE:
                        publishUpdatedDAOObjects(readWriteDAO.changeArchiveStatus(entity));
                        break;
                    default:
                        Notification.show("Something went wrong - button label = " + caption, Notification.Type.ERROR_MESSAGE);
                }
            }
        });
        */

        Embedded deleteAction = new Embedded(null, DELETE_ICON);
        deleteAction.setDescription("Unlike archiving, deleting is permanent.  Once you delete something you won't be able to get it back.  Or anything tied to it.");
        deleteAction.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                ConfirmDialog.show(layout.getUI(), "Delete " + entity.getSummaryDescription(), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            publishUpdatedDAOObjects(readWriteDAO.delete(entity));
                        }
                    }
                });
            }
        });
        /*
        Button deleteAction = new Button("Delete");
        deleteAction.setDescription("Unlike archiving, deleting is permanent.  Once you delete something you won't be able to get it back.  Or anything tied to it.");
        deleteAction.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                ConfirmDialog.show(layout.getUI(), "Delete " + entity.getSummaryDescription(), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            publishUpdatedDAOObjects(readWriteDAO.delete(entity));
                        }
                    }
                });
            }
        });
        archiveAction.addStyleName(Runo.BUTTON_SMALL);
        deleteButton.addStyleName(Runo.BUTTON_SMALL);
        */
        layout.addComponent(archiveAction, 0, 0);
        layout.setComponentAlignment(archiveAction, Alignment.MIDDLE_CENTER);
        layout.addComponent(deleteAction, 1, 0);
        layout.setComponentAlignment(deleteAction, Alignment.MIDDLE_CENTER);
        layout.setSpacing(true);
        return layout;
    }

    private void publishUpdatedDAOObjects(final ReadWriteDAO.ChainedUpdateSet<AppUserOwnedObject> daoResults) {
        for (AppUserOwnedObject modified : daoResults.getModifiedItems()) {
            eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, modified));
        }
        for (AppUserOwnedObject deleted : daoResults.getDeletedItems()) {
            eventBus.post(new IdObjectChanged<>(IdObjectChanged.ChangeType.DELETED, deleted));
        }
    }
}
