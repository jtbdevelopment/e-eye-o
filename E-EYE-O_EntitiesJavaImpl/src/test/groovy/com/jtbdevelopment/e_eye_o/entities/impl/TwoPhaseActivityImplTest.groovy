package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivityImpl

/**
 * Date: 11/30/13
 * Time: 4:04 PM
 */
class TwoPhaseActivityImplTest extends AbstractTwoPhaseActivityTest {
    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new TwoPhaseActivityImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }
}
