package com.jtbdevelopment.e_eye_o.entities;

import sun.plugin.dom.exception.InvalidStateException;

import java.security.InvalidParameterException;
import java.util.Collection;

/**
 * Date: 11/28/12
 * Time: 10:50 PM
 */
public abstract class AppUserOwnedObjectImpl extends IdObjectImpl implements AppUserOwnedObject {
    private AppUser appUser;

    public AppUserOwnedObjectImpl() {
    }

    public AppUserOwnedObjectImpl(final AppUser appUser) {
        setAppUser(appUser);
    }

    @Override
    public AppUser getAppUser() {
        return appUser;
    }

    @Override
    public <T extends AppUserOwnedObject> T setAppUser(final AppUser appUser) {
        if (this.appUser != null && !this.appUser.equals(appUser)) {
            throw new InvalidParameterException("Cannot reassign appuser after assignment");
        }
        this.appUser = appUser;
        return (T) this;
    }

}
