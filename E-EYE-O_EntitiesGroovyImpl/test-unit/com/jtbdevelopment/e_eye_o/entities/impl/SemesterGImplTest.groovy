package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/25/13
 * Time: 9:15 PM
 */
class SemesterGImplTest extends AbstractSemesterTest {

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new SemesterGImpl()
    }
}
