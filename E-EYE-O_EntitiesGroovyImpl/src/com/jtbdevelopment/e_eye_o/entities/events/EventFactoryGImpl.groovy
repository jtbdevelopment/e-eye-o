package com.jtbdevelopment.e_eye_o.entities.events

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 12/8/13
 * Time: 8:14 AM
 */
class EventFactoryGImpl implements EventFactory {
    @Override
    def <T extends AppUserOwnedObject> AppUserOwnedObjectChanged<T> newAppUserOwnedObjectChanged(final IdObjectChanged.ChangeType changeType, final T entity) {
        return new AppUserOwnedObjectChangedGImpl<T>(entity: entity, changeType: changeType)
    }

    @Override
    def <T extends IdObject> IdObjectChanged<T> newIdObjectChanged(final IdObjectChanged.ChangeType changeType, final T entity) {
        return new IdObjectChangedGImpl<T>(entity: entity, changeType: changeType)
    }
}
