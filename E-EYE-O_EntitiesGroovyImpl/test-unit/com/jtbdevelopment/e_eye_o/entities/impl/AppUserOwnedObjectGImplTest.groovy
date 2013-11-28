package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.security.InvalidParameterException

/**
 * Date: 11/25/13
 * Time: 8:59 PM
 */
class AppUserOwnedObjectGImplTest extends IdObjectGImplTest {

    public static class LocalEntity extends AppUserOwnedObjectGImpl {
    }

    @BeforeMethod
    def setup() {
        objectUnderTest = new LocalEntity()
    }

    @Test
    void testArchived() {
        testBooleanField("archived", false)
    }

    @Test
    void testAppUserField() {
        AppUserGImpl user = new AppUserGImpl();
        testNonStringFieldWithNullValidationError("appUser", null, user, AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR)
    }

    @Test
    public void testAssigningDifferentOwnerSameIDisOK() {
        AppUserGImpl userCopy1 = new AppUserGImpl();
        AppUserGImpl userCopy2 = new AppUserGImpl();
        userCopy1.id = "SAME"
        userCopy2.id = userCopy1.id

        objectUnderTest.appUser = userCopy1
        assert userCopy1.is(objectUnderTest.appUser)

        objectUnderTest.appUser = userCopy2
        assert userCopy2.is(objectUnderTest.appUser)
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testChangeAppUserExceptions() {
        AppUserGImpl userCopy1 = new AppUserGImpl();
        AppUserGImpl userCopy2 = new AppUserGImpl();
        userCopy1.id = "ONE"
        userCopy2.id = "TWO"
        objectUnderTest.appUser = userCopy1
        objectUnderTest.appUser = userCopy2
    }

}
