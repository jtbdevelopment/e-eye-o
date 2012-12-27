package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/5/12
 * Time: 11:05 PM
 */
public class AppUserImplTest extends AbstractAppUserOwnedObjectTest {

    @Test
    public void testSetFirstName() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError(AppUserImpl.class, "firstName", AppUser.FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR);
    }

    @Test
    public void testFirstNameSize() throws Exception {
        checkStringSizeValidation(AppUserImpl.class, "firstName", TOO_LONG_FOR_NAME, AppUser.APP_USER_FIRST_NAME_SIZE_ERROR);
    }

    @Test
    public void testSetLastName() throws Exception {
        checkStringSetGetsAndValidateNullsAsError(AppUserImpl.class, "lastName", AppUser.LAST_NAME_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testLastNameSize() throws Exception {
        checkStringSizeValidation(AppUserImpl.class, "lastName", TOO_LONG_FOR_NAME, AppUser.APP_USER_LAST_NAME_SIZE_ERROR);
    }

    @Test
    public void testSetEmailAddress() throws Exception {
        Method setter = getSetMethod(AppUserImpl.class, "emailAddress", String.class);
        Method getter = getGetMethod(AppUserImpl.class, "emailAddress");
        checkStringSetGetValidateSingleValue(AppUserImpl.class, getter, setter, VALID_EMAIL, false, AppUser.EMAIL_MUST_BE_A_VALID_FORMAT_ERROR);
        checkStringSetGetValidateSingleValue(AppUserImpl.class, getter, setter, NULL, true, AppUser.EMAIL_CANNOT_BE_NULL_ERROR);
        checkStringSetGetValidateSingleValue(AppUserImpl.class, getter, setter, INVALID_EMAIL, true, AppUser.EMAIL_MUST_BE_A_VALID_FORMAT_ERROR);
    }

    @Test
    public void testEmailSize() throws Exception {
        checkStringSizeValidation(AppUserImpl.class, "emailAddress", TOO_LONG_FOR_EMAIL, AppUser.APP_USER_EMAIL_SIZE_ERROR);
    }

    @Test
    public void testGetLastLoginDefaultsToNeverValue() {
        assertEquals(AppUser.NEVER_LOGGED_IN, new AppUserImpl().getLastLogin());
    }

    @Test
    public void testGetSetLastLogin() throws Exception {
        DateTime now = new DateTime();
        final AppUser appUser = new AppUserImpl().setLastLogin(now);
        assertEquals(now, appUser.getLastLogin());
        validateNotExpectingError(appUser, AppUser.LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR);
    }

    @Test
    public void testSetLastLoginNullExceptions() throws Exception {
        final AppUser appUser = new AppUserImpl().setLastLogin(null);
        validateExpectingError(appUser, AppUser.LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR);
    }
}
