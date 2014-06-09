package com.jtbdevelopment.e_eye_o.events

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.springframework.stereotype.Component

/**
 * Date: 12/8/13
 * Time: 8:14 AM
 */
@Component
class EventFactoryGImpl implements EventFactory {
    @Override
    def <T extends AppUserOwnedObject> AppUserOwnedObjectChanged<T> newAppUserOwnedObjectChanged(
            final IdObjectChanged.ChangeType changeType, final T entity) {
        return new AppUserOwnedObjectChangedGImpl<T>(entity: entity, changeType: changeType)
    }

    @Override
    def <T extends IdObject> IdObjectChanged<T> newIdObjectChanged(
            final IdObjectChanged.ChangeType changeType, final T entity) {
        return new IdObjectChangedGImpl<T>(entity: entity, changeType: changeType)
    }
}
