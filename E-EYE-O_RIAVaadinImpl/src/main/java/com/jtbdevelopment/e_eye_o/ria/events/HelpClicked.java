package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

/**
 * Date: 5/2/13
 * Time: 9:02 PM
 */
public class HelpClicked extends AppUserEvent {

    public HelpClicked(final AppUser appUser) {
        super(appUser);
    }
}
