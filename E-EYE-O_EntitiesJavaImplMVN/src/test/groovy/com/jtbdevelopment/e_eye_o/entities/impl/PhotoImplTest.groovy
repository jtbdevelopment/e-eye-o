package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.*

/**
 * Date: 11/30/13
 * Time: 12:32 PM
 */
class PhotoImplTest extends AbstractPhotoTest {
    @Override
    Observation createObservation() {
        return new ObservationImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new PhotoImpl()
    }
}
