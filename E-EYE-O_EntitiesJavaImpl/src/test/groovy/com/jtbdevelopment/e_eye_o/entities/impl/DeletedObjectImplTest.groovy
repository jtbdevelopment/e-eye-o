package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.DeletedObjectImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/30/13
 * Time: 4:16 PM
 */
class DeletedObjectImplTest extends AbstractDeletedObjectTest {
    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new DeletedObjectImpl()
    }
}
