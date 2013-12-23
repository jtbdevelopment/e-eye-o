package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/25/13
 * Time: 8:59 PM
 */
class AppUserOwnedObjectImplTest extends AbstractAppUserOwnedObjectTest {

    public static class LocalEntity extends AppUserOwnedObjectImpl {
        public LocalEntity() {
            super(null)
        }
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return (T) new LocalEntity()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

}
