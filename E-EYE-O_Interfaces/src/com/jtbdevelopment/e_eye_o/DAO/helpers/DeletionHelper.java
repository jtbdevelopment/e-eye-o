package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

/**
 * Date: 12/26/13
 * Time: 8:36 PM
 */
public interface DeletionHelper {
    void deleteUser(final AppUser user);

    <T extends AppUserOwnedObject> void delete(final T entity);
}
