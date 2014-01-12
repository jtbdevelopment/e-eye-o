package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.AppUserSettingsImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 12/1/13
 * Time: 8:18 AM
 */
class AppUserSettingsImplTest extends AbstractAppUserSettingsTest {
    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new AppUserSettingsImpl()
    }
}
