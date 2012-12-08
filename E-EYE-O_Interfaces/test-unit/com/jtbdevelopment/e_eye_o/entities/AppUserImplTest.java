package com.jtbdevelopment.e_eye_o.entities;

import static org.testng.Assert.*;

import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.security.InvalidParameterException;

/**
 * Date: 12/5/12
 * Time: 11:05 PM
 */
public class AppUserImplTest {
    private final String value = "JTB";
    private final String blank = "";

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetLoginNullExceptions() {
        new AppUserImpl().setLogin(null);
    }

    @Test(expectedExceptions = InvalidParameterException.class)
    public void testSetLoginBlankExceptions() {
        new AppUserImpl().setLogin(blank);
    }

    @Test
    public void testGetSetLogin() {
        assertEquals(value, new AppUserImpl().setLogin(value).getLogin());
    }

    @Test
    public void testSetFirstName() throws Exception {
        assertEquals(blank, new AppUserImpl().setFirstName(null).getFirstName());
        assertEquals(value, new AppUserImpl().setFirstName(value).getFirstName());
        assertEquals(blank, new AppUserImpl().setFirstName(blank).getFirstName());
    }

    @Test
    public void testSetLastName() throws Exception {
        assertEquals(blank, new AppUserImpl().setLastName(null).getLastName());
        assertEquals(value, new AppUserImpl().setLastName(value).getLastName());
        assertEquals(blank, new AppUserImpl().setLastName(blank).getLastName());
    }

    @Test
    public void testSetEmailAddress() throws Exception {
        assertEquals(value, new AppUserImpl().setEmailAddress(value).getEmailAddress());
        assertEquals(blank, new AppUserImpl().setEmailAddress(blank).getEmailAddress());
        assertEquals(blank, new AppUserImpl().setEmailAddress(null).getEmailAddress());
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
