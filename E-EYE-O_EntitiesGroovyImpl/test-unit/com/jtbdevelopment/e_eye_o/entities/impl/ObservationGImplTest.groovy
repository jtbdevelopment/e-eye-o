package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import com.jtbdevelopment.e_eye_o.entities.Student

/**
 * Date: 11/27/13
 * Time: 6:46 AM
 */
class ObservationGImplTest extends AbstractObservationTest {
    @Override
    ObservationCategory createCategory() {
        return new ObservationCategoryGImpl()
    }

    @Override
    Student createStudent() {
        return new StudentGImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new ObservationGImpl()
    }
}
