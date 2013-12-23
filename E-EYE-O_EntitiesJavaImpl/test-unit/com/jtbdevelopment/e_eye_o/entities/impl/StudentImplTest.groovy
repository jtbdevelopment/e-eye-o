package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.IdObject

/**
 * Date: 11/26/13
 * Time: 11:13 PM
 */
class StudentImplTest extends AbstractStudentTest {
    @Override
    ClassList createClassList(final AppUser user, boolean archived) {
        ClassList cl = new ClassListImpl(user);
        cl.setArchived(archived);
        return cl;
    }

    @Override
    def <T extends IdObject> T createObjectUnderTest() {
        return new StudentImpl()
    }

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }
}
