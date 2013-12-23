package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/30/13
 * Time: 4:04 PM
 */
class TwoPhaseActivityGImplTest extends AbstractTwoPhaseActivityTest {
    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new TwoPhaseActivityGImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }
}
