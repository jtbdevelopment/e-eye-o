package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

import java.security.InvalidParameterException;

/**
 * Date: 11/28/12
 * Time: 10:50 PM
 */
public abstract class AppUserOwnedObjectImpl extends IdObjectImpl implements AppUserOwnedObject {
    private AppUser appUser;
    private boolean archived = false;

    protected AppUserOwnedObjectImpl(final AppUser appUser) {
        setAppUser(appUser);
    }

    @Override
    public AppUser getAppUser() {
        return appUser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> T setAppUser(final AppUser appUser) {
        if (this.appUser != null && !this.appUser.equals(appUser)) {
            throw new InvalidParameterException("Cannot reassign appuser after assignment");
        }
        this.appUser = appUser;
        return (T) this;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> T setArchived(boolean archived) {
        this.archived = archived;
        return (T) this;
    }
}
