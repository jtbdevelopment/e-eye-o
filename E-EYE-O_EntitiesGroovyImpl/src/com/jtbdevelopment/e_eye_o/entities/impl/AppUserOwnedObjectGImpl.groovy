package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import groovy.transform.CompileStatic

import java.security.InvalidParameterException

/**
 * Date: 11/25/13
 * Time: 8:58 PM
 */
@CompileStatic
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
