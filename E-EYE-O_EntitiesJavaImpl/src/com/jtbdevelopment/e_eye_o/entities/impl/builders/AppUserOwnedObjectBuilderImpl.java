package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserOwnedObjectBuilder;

/**
 * Date: 3/9/13
 * Time: 11:52 AM
 */
public class AppUserOwnedObjectBuilderImpl<T extends AppUserOwnedObject> extends IdObjectBuilderImpl<T> implements AppUserOwnedObjectBuilder<T> {
    public AppUserOwnedObjectBuilderImpl(final T entity) {
        super(entity);
    }

    @Override
    public AppUserOwnedObjectBuilder<T> withAppUser(final AppUser appUser) {
        entity.setAppUser(appUser);
        return this;
    }

    @Override
    public AppUserOwnedObjectBuilder<T> withArchived(final boolean archived) {
        entity.setArchived(archived);
        return this;
    }
}
