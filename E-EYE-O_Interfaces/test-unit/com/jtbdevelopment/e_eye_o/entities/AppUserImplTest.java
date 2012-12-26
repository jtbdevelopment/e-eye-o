package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;

/**
 * Date: 12/5/12
 * Time: 11:05 PM
 */
public class AppUserImplTest extends AbstractIdObjectTest {

    @Test
    public void testSetFirstName() throws Exception {
        checkStringGetSetsAndValidateNullsAndBlanksAsError(AppUserImpl.class, "firstName", AppUser.FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR);
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
        //checkStringSetGetsWithNullsSavedAsBlanks(AppUserImpl.class, "emailAddress");
    }

    @Test
    public void testGetLastLoginDefaultsToNeverValue() {
        assertEquals(AppUser.NEVER_LOGGED_IN, new AppUserImpl().getLastLogin());
    }

    @Test
    public void testGetSetLastLogin() throws Exception {
        DateTime now = new DateTime();
        assertEquals(now, new AppUserImpl().setLastLogin(now).getLastLogin());
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetLastLoginNullExceptions() throws Exception {
        new AppUserImpl().setLastLogin(null);
    }
}
