package com.jtbdevelopment.e_eye_o.entities;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Date: 2/16/13
 * Time: 4:23 PM
 *
 * Represents a record of a deleted object
 * Useful for ATOM/RSS style listing
 */
public interface DeletedObject extends AppUserOwnedObject {
    final static String DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR = "DeletedObject.deletedId" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    @NotEmpty(message = DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR)
    String getDeletedId();

    DeletedObject setDeletedId(final String deletedId);
}
