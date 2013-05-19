package com.jtbdevelopment.e_eye_o.ria.vaadin.components.tabs;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TabComponent;

/**
 * Date: 3/10/13
 * Time: 3:35 PM
 */
public class IdObjectRelatedTab extends Tab {
    private final TabComponent.IdObjectTabs idObjectTab;

    public IdObjectRelatedTab(final TabComponent.IdObjectTabs sideTab, final EventBus eventBus) {
        super(sideTab.getCaption(), eventBus);
        this.idObjectTab = sideTab;
    }

    public TabComponent.IdObjectTabs getIdObjectTab() {
        return idObjectTab;
    }
}
