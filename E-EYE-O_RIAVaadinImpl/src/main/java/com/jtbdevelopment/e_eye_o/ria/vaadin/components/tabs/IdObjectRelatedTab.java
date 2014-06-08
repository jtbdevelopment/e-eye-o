package com.jtbdevelopment.e_eye_o.ria.vaadin.components.tabs;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;

/**
 * Date: 3/10/13
 * Time: 3:35 PM
 */
public class IdObjectRelatedTab extends Tab {
    private final IdObjectEntitySettings entitySettings;

    public IdObjectRelatedTab(final IdObjectEntitySettings entitySettings, final EventBus eventBus) {
        super(entitySettings.plural(), eventBus);
        this.entitySettings = entitySettings;
    }

    public IdObjectRelatedTab(final Class<? extends IdObject> entityType, final EventBus eventBus) {
        this(entityType.getAnnotation(IdObjectEntitySettings.class), eventBus);
    }

    public IdObjectEntitySettings getIdObjectTab() {
        return entitySettings;
    }
}
