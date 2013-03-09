package com.jtbdevelopment.e_eye_o.ria.vaadin.events;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 3/8/13
 * Time: 10:52 PM
 */
public class TreeSelectionChanged {
    private Class<? extends IdObject> entityType;
    private String entityId;

    public Class<? extends IdObject> getEntityType() {
        return entityType;
    }

    public void setEntityType(final Class<? extends IdObject> entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(final String entityId) {
        this.entityId = entityId;
    }
}
