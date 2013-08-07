package com.jtbdevelopment.e_eye_o.entities.events;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 8/5/13
 * Time: 10:39 PM
 */
public interface EventFactory {

    <T extends AppUserOwnedObject> AppUserOwnedObjectChanged<T> newAppUserOwnedObjectChanged(final IdObjectChanged.ChangeType changeType, final T entity);

    <T extends IdObject> IdObjectChanged<T> newIdObjectChanged(final IdObjectChanged.ChangeType changeType, final T entity);

}
