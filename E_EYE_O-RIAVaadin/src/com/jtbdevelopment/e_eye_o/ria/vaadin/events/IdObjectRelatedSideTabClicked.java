package com.jtbdevelopment.e_eye_o.ria.vaadin.events;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.SideTabComponent;

/**
 * Date: 3/10/13
 * Time: 3:58 PM
 */
public class IdObjectRelatedSideTabClicked {
    private final SideTabComponent.IdObjectSideTabs entityType;

    public IdObjectRelatedSideTabClicked(final SideTabComponent.IdObjectSideTabs entityType) {
        this.entityType = entityType;
    }

    public SideTabComponent.IdObjectSideTabs getEntityType() {
        return entityType;
    }
}
