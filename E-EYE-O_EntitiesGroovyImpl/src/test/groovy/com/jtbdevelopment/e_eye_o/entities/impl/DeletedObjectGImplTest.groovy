package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserGImpl
import com.jtbdevelopment.e_eye_o.entities.DeletedObjectGImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/30/13
 * Time: 4:16 PM
 */
class DeletedObjectGImplTest extends AbstractDeletedObjectTest {
    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new DeletedObjectGImpl()
    }
}
