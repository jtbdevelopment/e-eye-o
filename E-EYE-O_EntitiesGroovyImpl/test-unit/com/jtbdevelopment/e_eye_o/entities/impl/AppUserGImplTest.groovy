package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 11/21/13
 * Time: 7:09 AM
 */
class AppUserGImplTest extends IdObjectGImplTest {
    @BeforeMethod
    def setup() {
        objectUnderTest = new AppUserGImpl()
    }

    @Test
    void testFirstName() {
        testStringFieldExpectingErrorForNullOrBlank("firstName", BLANK_STRING, AppUser.FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR)
        testStringFieldSize("firstName", IdObject.MAX_NAME_SIZE, AppUser.APP_USER_FIRST_NAME_SIZE_ERROR)
    }

    @Test
    void testLastName() {
        testStringFieldExpectingErrorForNullOnly("lastName", BLANK_STRING, AppUser.LAST_NAME_CANNOT_BE_NULL_ERROR)
        testStringFieldSize("lastName", IdObject.MAX_NAME_SIZE, AppUser.APP_USER_LAST_NAME_SIZE_ERROR)
    }

    @Test
    void testPassword() {
        testStringFieldExpectingErrorForNullOrBlank("password", BLANK_STRING, AppUser.APP_PASSWORD_CANNOT_BE_NULL_ERROR)
        testStringFieldSize("password", AppUser.MAX_PASSWORD_SIZE, AppUser.APP_PASSWORD_SIZE_ERROR)
    }

    @Test
    void testLastLogout() {
        testNonStringFieldWithNullValidationError("lastLogout", AppUser.NEVER_LOGGED_IN, new DateTime(), AppUser.LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR)
    }

    @Test
    void testIsActive() {
        testBooleanField("active", true);
    }

    @Test
    void testIsAdmin() {
        testBooleanField("admin", false)
    }

    @Test
    void testActivated() {
        testBooleanField("activated", false)
    }

    @Test
    @Override
    public void testSummaryDescription() {
        objectUnderTest.firstName = " First";
        objectUnderTest.lastName = " Last";
        assert "First Last" == objectUnderTest.summaryDescription
    }

    @Test
    public void testSummaryNoLast() {
        objectUnderTest.firstName = "First ";
        objectUnderTest.lastName = " ";
        assert "First " == objectUnderTest.summaryDescription
    }
}
