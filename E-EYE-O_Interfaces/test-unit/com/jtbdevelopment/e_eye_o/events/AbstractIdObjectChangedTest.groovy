package com.jtbdevelopment.e_eye_o.events

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/7/13
 * Time: 9:35 PM
 */
abstract class AbstractIdObjectChangedTest {
    IdObjectChanged objectUnderTest
    AppUser user

    abstract IdObjectChanged createIdObjectChanged(
            final IdObject idObject, final IdObjectChanged.ChangeType changeType);

    abstract AppUser createAppUser()

    @BeforeMethod
    def setUp() {
        user = createAppUser()
        objectUnderTest = createIdObjectChanged(user, IdObjectChanged.ChangeType.UPDATED)
    }

    @Test
    void testGetEntityType() {
        assert user.class == objectUnderTest.getEntityType()
    }

    @Test
    void testGetChangeType() {
        assert IdObjectChanged.ChangeType.UPDATED == objectUnderTest.changeType
    }

    @Test
    void testGetEntity() {
        assert user == objectUnderTest.entity
    }
}
