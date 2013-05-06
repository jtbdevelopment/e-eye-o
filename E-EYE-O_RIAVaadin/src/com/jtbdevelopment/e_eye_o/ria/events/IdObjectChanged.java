package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 3/14/13
 * Time: 5:26 PM
 */
public class IdObjectChanged<T extends IdObject> {
    public enum ChangeType {
        ADDED,
        MODIFIED,
        DELETED
    }

    private ChangeType changeType;
    private T entity;

    public IdObjectChanged(final ChangeType changeType, final T entity) {
        this.entity = entity;
        this.changeType = changeType;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityType() {
        return (Class<T>) entity.getClass();
    }

    public T getEntity() {
        return entity;
    }
}
