package com.jtbdevelopment.e_eye_o.entities.events;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

/**
 * Date: 5/19/13
 * Time: 10:18 AM
 */
public class AppUserOwnedObjectChangedImpl<T extends AppUserOwnedObject> extends IdObjectChangedImpl<T> implements AppUserOwnedObjectChanged<T> {
    public AppUserOwnedObjectChangedImpl(final ChangeType changeType, final T entity) {
        super(changeType, entity);
    }

    @Override
    public T getEntity() {
        return super.getEntity();
    }
}
