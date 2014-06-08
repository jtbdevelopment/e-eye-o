package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserGImpl
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObjectGImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/25/13
 * Time: 8:59 PM
 */
class AppUserOwnedObjectGImplTest extends AbstractAppUserOwnedObjectTest {

    public static class LocalEntity extends AppUserOwnedObjectGImpl {
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new LocalEntity()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

}
