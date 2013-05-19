package com.jtbdevelopment.e_eye_o.ria.events;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

/**
 * Date: 5/19/13
 * Time: 10:18 AM
 */
public class AppUserOwnedObjectChanged<T extends AppUserOwnedObject> extends IdObjectChanged<T> {
    public AppUserOwnedObjectChanged(final ChangeType changeType, final T entity) {
        super(changeType, entity);
    }

    @Override
    public T getEntity() {
        return super.getEntity();
    }
}
