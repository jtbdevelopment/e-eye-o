package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/21/13
 * Time: 7:09 AM
 */
class AppUserImplTest extends AbstractAppUserTest {
    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new AppUserImpl()
    }
}
