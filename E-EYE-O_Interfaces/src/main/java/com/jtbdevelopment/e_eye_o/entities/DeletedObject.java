package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Date: 2/16/13
 * Time: 4:23 PM
 * <p/>
 * Represents a record of a deleted object
 * Useful for ATOM/RSS style listing for knowing something has changed about an object which no longer exists
 * <p/>
 * It is generally expected that
 * a)  these objects cannot be created or modified or deleted directly via ReadWriteDAO.create/update/delete.  These calls will exception in these cases
 * b)  these will be provided as part of result listings of #ReadOnlyDAO.getModifiedSince
 * <p/>
 * The DAO can model this object in the store or generate on the fly as desired
 */
@IdObjectEntitySettings(viewable = true, editable = false, defaultSortField = "deletedId", singular = "Deleted Item", plural = "Deleted Items")
public interface DeletedObject extends AppUserOwnedObject {
    final static String DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR = "DeletedObject.deletedId" + CANNOT_BE_BLANK_OR_NULL_ERROR;

    @NotEmpty(message = DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @IdObjectFieldSettings(editableBy = IdObjectFieldSettings.EditableBy.NONE, label = "Deleted Object Id")
    String getDeletedId();

    void setDeletedId(final String deletedId);
}
