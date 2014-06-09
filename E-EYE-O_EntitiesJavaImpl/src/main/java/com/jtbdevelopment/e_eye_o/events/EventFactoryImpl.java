package com.jtbdevelopment.e_eye_o.events;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.springframework.stereotype.Component;

/**
 * Date: 8/5/13
 * Time: 10:41 PM
 */
@Component
@SuppressWarnings("unused")
public class EventFactoryImpl implements EventFactory {
    @Override
    @SuppressWarnings("unchecked")
    public <T extends AppUserOwnedObject> AppUserOwnedObjectChanged<T> newAppUserOwnedObjectChanged(final IdObjectChanged.ChangeType changeType, final T entity) {
        return new AppUserOwnedObjectChangedImpl(changeType, entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> IdObjectChanged<T> newIdObjectChanged(final IdObjectChanged.ChangeType changeType, final T entity) {
        return new IdObjectChangedImpl(changeType, entity);
    }
}
