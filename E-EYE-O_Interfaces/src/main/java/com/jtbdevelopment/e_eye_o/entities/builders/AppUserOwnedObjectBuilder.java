package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

/**
 * Date: 3/9/13
 * Time: 11:14 AM
 */
public interface AppUserOwnedObjectBuilder<T extends AppUserOwnedObject> extends IdObjectBuilder<T> {
    AppUserOwnedObjectBuilder<T> withAppUser(final AppUser appUser);

    AppUserOwnedObjectBuilder<T> withArchived(final boolean archived);
}
