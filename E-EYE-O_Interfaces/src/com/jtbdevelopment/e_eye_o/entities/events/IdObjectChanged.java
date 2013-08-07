package com.jtbdevelopment.e_eye_o.entities.events;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 8/5/13
 * Time: 10:29 PM
 */
public interface IdObjectChanged<T extends IdObject> {
    ChangeType getChangeType();

    @SuppressWarnings("unchecked")
    Class<T> getEntityType();

    T getEntity();

    public enum ChangeType {
        ADDED,
        MODIFIED,
        DELETED
    }
}
