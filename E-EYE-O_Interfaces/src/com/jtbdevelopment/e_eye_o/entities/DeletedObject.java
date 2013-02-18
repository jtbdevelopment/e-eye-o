package com.jtbdevelopment.e_eye_o.entities;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Date: 2/16/13
 * Time: 4:23 PM
 * <p/>
 * Represents a record of a deleted object
 * Useful for ATOM/RSS style listing for knowing something has changed about an object which no longer exits
 * <p/>
 * It is generally expected that
 * a)  these will automatically be created by DAO service as appropriate
 * b)  DAO service will prevent updates on deletedobject
 * c)  DAO service will prevent deletes of deletedobject
 * d)  DAO service will automatically remove these if user is deleted
 * c)  DAO service will filter out of query results unless
 * i)  explicitly called for
 * ii) looking for a time based change list
 */
public interface DeletedObject extends AppUserOwnedObject {
    final static String DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR = "DeletedObject.deletedId" + CANNOT_BE_BLANK_OR_NULL_ERROR;

    @NotEmpty(message = DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR)
    String getDeletedId();

    DeletedObject setDeletedId(final String deletedId);
}
