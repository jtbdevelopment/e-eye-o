package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;

/**
 * Date: 3/10/13
 * Time: 3:58 PM
 */
public class IdObjectRelatedSideTabClicked extends AppUserEvent {
    private final IdObjectEntitySettings entitySettings;

    public IdObjectRelatedSideTabClicked(final AppUser appUser, final IdObjectEntitySettings entitySettings) {
        super(appUser);
        this.entitySettings = entitySettings;
    }

    public IdObjectEntitySettings getEntitySettings() {
        return entitySettings;
    }
}
