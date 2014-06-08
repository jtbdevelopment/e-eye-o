package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

/**
 * Date: 5/7/13
 * Time: 11:41 AM
 */
public class SettingsClicked extends AppUserEvent {
    public SettingsClicked(final AppUser appUser) {
        super(appUser);
    }
}
