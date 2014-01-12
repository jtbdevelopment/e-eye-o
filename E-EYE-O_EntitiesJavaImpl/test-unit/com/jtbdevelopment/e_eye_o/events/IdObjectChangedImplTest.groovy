package com.jtbdevelopment.e_eye_o.events

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 12/7/13
 * Time: 9:35 PM
 */
class IdObjectChangedImplTest extends AbstractIdObjectChangedTest {

    @Override
    IdObjectChanged createIdObjectChanged(final IdObject idObject, final IdObjectChanged.ChangeType changeType) {
        return new IdObjectChangedImpl<>(changeType, idObject)
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }
}
