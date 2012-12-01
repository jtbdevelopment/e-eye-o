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
    public AppUserOwnedObject setAppUser(final AppUser appUser) {
        if (this.appUser != null && !this.appUser.getId().equals(appUser.getId())) {
            throw new InvalidStateException("Cannot reassign appuser after assignment");
        }
        validateNonNullValue(appUser);
        this.appUser = appUser;
        return this;
    }

    protected void validateSameAppUser(final AppUserOwnedObject otherObject) {
        validateNonNullValue(otherObject);
        if (this.appUser == null && otherObject.getAppUser() == null) {
            throw new InvalidStateException("Neither object for comparison is owned");
        }
        if (this.appUser == null) {
            throw new InvalidStateException("No appUser for this object");
        } else {
            if (!this.appUser.equals(otherObject.getAppUser())) {
                throw new InvalidParameterException("App Users Do Not Match");
            }
        }
    }

    protected <T extends AppUserOwnedObject> void validateSameAppUsers(final Collection<T> others) {
        validateNonNullValue(others);
        for (AppUserOwnedObject otherObject : others) {
            validateSameAppUser(otherObject);
        }
    }

}
