package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject

import java.security.InvalidParameterException

/**
 * Date: 11/25/13
 * Time: 8:58 PM
 */
abstract class AppUserOwnedObjectGImpl extends IdObjectGImpl implements AppUserOwnedObject {
    AppUser appUser
    boolean archived = false

    void setAppUser(final AppUser appUser) {
        if (this.appUser != null && !this.appUser.equals(appUser)) {
            throw new InvalidParameterException("Cannot reassign appuser after assignment");
        }
        this.appUser = appUser;
    }
}
