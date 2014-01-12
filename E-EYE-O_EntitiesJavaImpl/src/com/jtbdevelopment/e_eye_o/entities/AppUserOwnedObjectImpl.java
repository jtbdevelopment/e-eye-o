package com.jtbdevelopment.e_eye_o.entities;

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
    public void setAppUser(final AppUser appUser) {
        if (this.appUser != null && !this.appUser.equals(appUser)) {
            throw new InvalidParameterException("Cannot reassign appuser after assignment");
        }
        this.appUser = appUser;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
