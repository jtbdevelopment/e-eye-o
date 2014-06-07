package com.jtbdevelopment.e_eye_o.events;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

/**
 * Date: 8/5/13
 * Time: 10:30 PM
 */
public interface AppUserOwnedObjectChanged<T extends AppUserOwnedObject> extends IdObjectChanged<T> {
    @Override
    T getEntity();
}
