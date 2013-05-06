package com.jtbdevelopment.e_eye_o.ria.vaadin.components.tabs;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectRelatedSideTabClicked;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TabComponent;

/**
 * Date: 3/10/13
 * Time: 3:35 PM
 */
public class IdObjectRelatedTab extends Tab {
    public IdObjectRelatedTab(TabComponent.IdObjectTabs sideTab, final EventBus eventBus) {
        super(sideTab.getCaption(), eventBus, new IdObjectRelatedSideTabClicked(sideTab));
    }
}
