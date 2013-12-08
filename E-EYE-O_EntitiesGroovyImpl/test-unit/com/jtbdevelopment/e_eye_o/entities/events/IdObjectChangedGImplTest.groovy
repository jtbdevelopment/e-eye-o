package com.jtbdevelopment.e_eye_o.entities.events

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/7/13
 * Time: 9:35 PM
 */
class IdObjectChangedGImplTest {
    def objectUnderTest
    def user

    @BeforeMethod
    def setUp() {
        user = new AppUserGImpl()
        objectUnderTest = new IdObjectChangedGImpl<AppUser>(entity: user, changeType: IdObjectChanged.ChangeType.MODIFIED)
    }

    @Test
    void testGetEntityType() {
        assert user.class == objectUnderTest.getEntityType()
    }

    @Test
    void testGetChangeType() {
        assert IdObjectChanged.ChangeType.MODIFIED == objectUnderTest.changeType
    }

    @Test
    void testGetEntity() {
        assert user == objectUnderTest.entity
    }
}
