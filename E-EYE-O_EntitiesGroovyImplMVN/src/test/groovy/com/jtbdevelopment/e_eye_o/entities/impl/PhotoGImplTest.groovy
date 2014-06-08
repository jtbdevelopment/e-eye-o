package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.*

/**
 * Date: 11/30/13
 * Time: 12:32 PM
 */
class PhotoGImplTest extends AbstractPhotoTest {
    @Override
    Observation createObservation() {
        return new ObservationGImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new PhotoGImpl()
    }
}
