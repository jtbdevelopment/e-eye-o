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
    public void testSetGetLogin() {
        checkStringSetGetsWithBlanksAndNullsAsException(AppUserImpl.class, "login");
    }

    @Test
    public void testSetFirstName() throws Exception {
        checkStringSetGetsWithNullsSavedAsBlanks(AppUserImpl.class, "firstName");
    }

    @Test
    public void testSetLastName() throws Exception {
        checkStringSetGetsWithNullsSavedAsBlanks(AppUserImpl.class, "lastName");
    }

    @Test
    public void testSetEmailAddress() throws Exception {
        checkStringSetGetsWithNullsSavedAsBlanks(AppUserImpl.class, "emailAddress");
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
