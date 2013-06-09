package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.ria.events.AppUserOwnedObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectChanged;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * Date: 5/12/13
 * Time: 5:20 PM
 */
public class ArchiveAndDeleteButtons<T extends AppUserOwnedObject> extends CustomComponent {
    private static final Logger logger = LoggerFactory.getLogger(ArchiveAndDeleteButtons.class);

    private static final ThemeResource ARCHIVE_ICON = new ThemeResource("icons/16/power_off.png");
    private static final ThemeResource ACTIVATE_ICON = new ThemeResource("icons/16/power_on.png");
    private static final ThemeResource DELETE_ICON = new ThemeResource("icons/16/delete.png");

    private final EventBus eventBus;

    public ArchiveAndDeleteButtons(final ReadWriteDAO readWriteDAO, final EventBus eventBus, final T entity) {
        this.eventBus = eventBus;

        setSizeUndefined();
        final GridLayout layout;
        layout = new GridLayout(2, 1);

        Embedded archiveAction;
        if (entity.isArchived()) {
            archiveAction = new Embedded(null, ACTIVATE_ICON);
            archiveAction.setDescription("Re-activating an item puts it back in your default lists.  Re-activating an item will re-activate it's related items.");
        } else {
            archiveAction = new Embedded(null, ARCHIVE_ICON);
            archiveAction.setDescription("Archiving anything does not delete it, just drops it off your lists by default.  You can always always re-activate them.  Archiving something will archive related items.");
        }
        archiveAction.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                logger.trace(UI.getCurrent().getSession().getAttribute(AppUser.class).getId() + ": archive/unarchive called on " + entity.getId());
                publishUpdatedDAOObjects(readWriteDAO.changeArchiveStatus(entity));
            }
        });

        Embedded deleteAction = new Embedded(null, DELETE_ICON);
        deleteAction.setDescription("Unlike archiving, deleting is permanent.  Once you delete something you won't be able to get it back.  Or anything tied to it.");
        deleteAction.addClickListener(new MouseEvents.ClickListener() {
            @Override
            public void click(MouseEvents.ClickEvent event) {
                logger.trace(UI.getCurrent().getSession().getAttribute(AppUser.class).getId() + ": delete icon clicked on " + entity.getId());
                ConfirmDialog.show(layout.getUI(), "Delete " + entity.getSummaryDescription(), new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            logger.trace(UI.getCurrent().getSession().getAttribute(AppUser.class).getId() + ": delete dialog confirmed on " + entity.getId());
                            publishUpdatedDAOObjects(readWriteDAO.delete(entity));
                        }
                    }
                });
            }
        });
        layout.addComponent(archiveAction, 0, 0);
        layout.setComponentAlignment(archiveAction, Alignment.MIDDLE_CENTER);
        layout.addComponent(deleteAction, 1, 0);
        layout.setComponentAlignment(deleteAction, Alignment.MIDDLE_CENTER);
        layout.setSpacing(true);
        layout.setSizeUndefined();
        setCompositionRoot(layout);
    }

    private void publishUpdatedDAOObjects(final ReadWriteDAO.ChainedUpdateSet<AppUserOwnedObject> daoResults) {
        for (AppUserOwnedObject modified : daoResults.getModifiedItems()) {
            eventBus.post(new AppUserOwnedObjectChanged<>(IdObjectChanged.ChangeType.MODIFIED, modified));
        }
        for (AppUserOwnedObject deleted : daoResults.getDeletedItems()) {
            eventBus.post(new AppUserOwnedObjectChanged<>(IdObjectChanged.ChangeType.DELETED, deleted));
        }
    }
}
