package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.SemesterImpl

/**
 * Date: 11/25/13
 * Time: 9:15 PM
 */
class SemesterImplTest extends AbstractSemesterTest {

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new SemesterImpl()
    }
}
