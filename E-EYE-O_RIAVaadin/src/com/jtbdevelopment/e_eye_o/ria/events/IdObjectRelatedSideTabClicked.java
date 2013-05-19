package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TabComponent;

/**
 * Date: 3/10/13
 * Time: 3:58 PM
 */
public class IdObjectRelatedSideTabClicked extends AppUserEvent {
    private final TabComponent.IdObjectTabs entityType;

    public IdObjectRelatedSideTabClicked(final AppUser appUser, final TabComponent.IdObjectTabs entityType) {
        super(appUser);
        this.entityType = entityType;
    }

    public TabComponent.IdObjectTabs getEntityType() {
        return entityType;
    }
}
