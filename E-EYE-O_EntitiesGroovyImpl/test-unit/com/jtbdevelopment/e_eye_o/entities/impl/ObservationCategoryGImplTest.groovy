package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/26/13
 * Time: 6:58 AM
 */
class ObservationCategoryGImplTest extends AbstractObservationCategoryTest {
    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new ObservationCategoryGImpl()
    }
}
