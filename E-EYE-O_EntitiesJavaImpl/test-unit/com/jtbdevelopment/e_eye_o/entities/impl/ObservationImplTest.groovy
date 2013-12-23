package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import com.jtbdevelopment.e_eye_o.entities.Student

/**
 * Date: 11/27/13
 * Time: 6:46 AM
 */
class ObservationImplTest extends AbstractObservationTest {
    @Override
    ObservationCategory createCategory() {
        return new ObservationCategoryImpl()
    }

    @Override
    Student createStudent() {
        return new StudentImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new ObservationImpl()
    }
}
