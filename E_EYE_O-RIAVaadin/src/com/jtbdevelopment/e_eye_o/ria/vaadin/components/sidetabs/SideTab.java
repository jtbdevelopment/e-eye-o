package com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs;

import com.google.common.eventbus.EventBus;
import com.vaadin.ui.Label;

/**
 * Date: 3/10/13
 * Time: 3:34 PM
 */
public class SideTab extends Label {
    private final EventBus eventBus;
    private final Object event;

    public SideTab(final String caption, final EventBus eventBus, final Object messageToPublish) {
        super(caption);
        addStyleName("sidetabs");
//        setWidth(null);
        this.eventBus = eventBus;
        this.event = messageToPublish;
    }

    public void onClicked() {
        if (eventBus != null && event != null) {
            eventBus.post(event);
        }
    }
}
