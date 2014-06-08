package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

/**
 * Date: 5/19/13
 * Time: 10:14 AM
 */
public abstract class AppUserEvent {
    protected final AppUser appUser;

    public AppUserEvent(final AppUser appUser) {
        this.appUser = appUser;
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
