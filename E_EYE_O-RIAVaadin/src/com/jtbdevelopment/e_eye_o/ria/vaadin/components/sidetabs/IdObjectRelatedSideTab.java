package com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.SideTabComponent;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectRelatedSideTabClicked;

/**
 * Date: 3/10/13
 * Time: 3:35 PM
 */
public class IdObjectRelatedSideTab extends SideTab {
    public IdObjectRelatedSideTab(SideTabComponent.IdObjectSideTabs sideTab, final EventBus eventBus) {
        super(sideTab.getCaption(), eventBus, new IdObjectRelatedSideTabClicked(sideTab));
    }

}
