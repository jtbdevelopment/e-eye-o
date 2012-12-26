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

    protected void validateSameAppUser(final AppUserOwnedObject otherObject) {
        if (this.appUser == null) {
            throw new InvalidStateException("No appUser for this object");
        }
        if (otherObject == null || otherObject.getAppUser() == null) {
            throw new InvalidStateException("Neither object for comparison is owned");
        }
        if (!this.appUser.equals(otherObject.getAppUser())) {
            throw new InvalidParameterException("App Users Do Not Match");
        }
    }

    protected <T extends AppUserOwnedObject> void validateSameAppUsers(final Collection<T> others) {
        if (others != null) {
            for (AppUserOwnedObject otherObject : others) {
                validateSameAppUser(otherObject);
            }
        }
    }

}
