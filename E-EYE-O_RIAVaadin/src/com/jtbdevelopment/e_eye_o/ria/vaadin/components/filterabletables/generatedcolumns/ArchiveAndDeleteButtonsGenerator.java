package com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.generatedcolumns;

import com.jtbdevelopment.e_eye_o.DAO.helpers.ArchiveHelper;
import com.jtbdevelopment.e_eye_o.DAO.helpers.IdObjectDeletionHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.AllItemsBeanItemContainer;
import com.vaadin.ui.Table;

/**
 * Date: 3/16/13
 * Time: 6:42 PM
 */
public class ArchiveAndDeleteButtonsGenerator<T extends AppUserOwnedObject> implements Table.ColumnGenerator {
    private final ArchiveHelper archiveHelper;
    private final IdObjectDeletionHelper idObjectDeletionHelper;
    private final AllItemsBeanItemContainer<T> entities;

    public ArchiveAndDeleteButtonsGenerator(final ArchiveHelper archiveHelper, final IdObjectDeletionHelper idObjectDeletionHelper, final AllItemsBeanItemContainer<T> entities) {
        this.entities = entities;
        this.archiveHelper = archiveHelper;
        this.idObjectDeletionHelper = idObjectDeletionHelper;
    }

    @Override
    public Object generateCell(final Table source, final Object itemId, final Object columnId) {
        final T entity = entities.getItem(itemId).getBean();

        return new ArchiveAndDeleteButtons<>(archiveHelper, idObjectDeletionHelper, entity);
    }
}
