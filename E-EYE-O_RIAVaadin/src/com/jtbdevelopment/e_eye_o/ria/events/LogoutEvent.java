package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

/**
 * Date: 3/10/13
 * Time: 3:44 PM
 */
public class LogoutEvent extends AppUserEvent {

    public LogoutEvent(final AppUser appUser) {
        super(appUser);
    }

}
