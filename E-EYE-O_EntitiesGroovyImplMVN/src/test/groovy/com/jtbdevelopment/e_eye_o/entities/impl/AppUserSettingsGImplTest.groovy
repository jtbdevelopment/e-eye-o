package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserGImpl
import com.jtbdevelopment.e_eye_o.entities.AppUserSettingsGImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 12/1/13
 * Time: 8:18 AM
 */
class AppUserSettingsGImplTest extends AbstractAppUserSettingsTest {
    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new AppUserSettingsGImpl()
    }
}
