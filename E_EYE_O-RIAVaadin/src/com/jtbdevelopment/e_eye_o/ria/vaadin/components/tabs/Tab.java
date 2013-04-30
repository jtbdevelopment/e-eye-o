package com.jtbdevelopment.e_eye_o.ria.vaadin.components.tabs;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.ui.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 3/10/13
 * Time: 3:34 PM
 */
public class Tab extends Label {
    private static final Logger logger = LoggerFactory.getLogger(Tab.class);
    private final EventBus eventBus;
    private final Object event;

    public Tab(final String caption, final EventBus eventBus, final Object messageToPublish) {
        super(caption);
        addStyleName("tabs");
        setWidth(null);
        this.eventBus = eventBus;
        this.event = messageToPublish;
    }

    public void onClicked() {
        logger.trace(getUI().getSession().getAttribute(AppUser.class).getId() + ": clicked on Tab " + getCaption());
        if (eventBus != null && event != null) {
            eventBus.post(event);
        }
    }
}
