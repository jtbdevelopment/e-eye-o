package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 6/8/13
 * Time: 4:23 PM
 */
public interface IdObjectUpdateHelper {
    /**
     * Based on IdObjectFieldSettings annotation and the updating user, will undo changes on updatedEntity
     * not permissioned for the user
     * <p/>
     * updatedEntity is updated in place
     *
     * @param updatingUser  - user performing update
     * @param currentEntity - current saved version of object
     * @param updatedEntity - proposed update
     * @param <T>           - entity type
     */
    <T extends IdObject> void validateUpdates(final AppUser updatingUser, final T currentEntity, final T updatedEntity);
}
