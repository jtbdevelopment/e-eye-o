package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.ObservableImpl

/**
 * Date: 11/26/13
 * Time: 6:49 AM
 */
class ObservableImplTest extends AbstractObservableTest {
    class LocalEntity extends ObservableImpl {
        public LocalEntity() {
            super(null)
        }
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return (T) new LocalEntity()
    }
}
