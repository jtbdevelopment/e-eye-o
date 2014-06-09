package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject

/**
 * Date: 12/1/13
 * Time: 3:40 PM
 */
class AppUserOwnedObjectBuilderGImpl<T extends AppUserOwnedObject> extends IdObjectBuilderGImpl<T> implements AppUserOwnedObjectBuilder<T> {
    @Override
    AppUserOwnedObjectBuilder<T> withAppUser(final AppUser appUser) {
        entity.appUser = appUser
        return this;
    }

    @Override
    AppUserOwnedObjectBuilder<T> withArchived(final boolean archived) {
        entity.archived = archived
        return this;
    }
}
