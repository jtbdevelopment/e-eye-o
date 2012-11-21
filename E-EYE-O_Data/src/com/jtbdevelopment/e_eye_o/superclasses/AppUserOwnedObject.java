package com.jtbdevelopment.e_eye_o.superclasses;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.security.InvalidParameterException;

/**
 * Date: 11/19/12
 * Time: 10:21 PM
 */
@MappedSuperclass
public abstract class AppUserOwnedObject extends IdObject {
    private AppUser appUser;

    @SuppressWarnings("unused")
    protected AppUserOwnedObject() {
        //  For hibernate
    }

    public AppUserOwnedObject(final AppUser appUser) {
        setAppUser(appUser);
    }

    @ManyToOne
    public AppUser getAppUser() {
        return appUser;
    }

    private void setAppUser(final AppUser appUser) {
        if( appUser == null ) {
            throw new InvalidParameterException("appUser cannot be null");
        }
        this.appUser = appUser;
    }
}
