package com.jtbdevelopment.e_eye_o.entities.events

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl

/**
 * Date: 12/7/13
 * Time: 9:35 PM
 */
class IdObjectChangedGImplTest extends AbstractIdObjectChangedTest {

    @Override
    IdObjectChanged createIdObjectChanged(final IdObject idObject, final IdObjectChanged.ChangeType changeType) {
        return new IdObjectChangedGImpl<AppUser>(entity: idObject, changeType: changeType)
    }

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }
}
