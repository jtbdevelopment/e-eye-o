package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/13/13
 * Time: 7:55 PM
 */
public class HibernateAppUserTest extends HibernateAbstractIdObjectTest {
    private AppUser implAppUser;
    private HibernateAppUser hibernateAppUser;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        implAppUser = context.mock(AppUser.class, "default");
        hibernateAppUser = new HibernateAppUser(implAppUser);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(AppUser.class);
            will(returnValue(implAppUser));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implAppUser, new HibernateAppUser().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final AppUser local = context.mock(AppUser.class, "local");
        assertSame(local, new HibernateAppUser(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final AppUser local = context.mock(AppUser.class, "local");
        assertSame(local, new HibernateAppUser(new HibernateAppUser(local)).getWrapped());
    }


    @Test
    public void testGetFirstName() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).getFirstName();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateAppUser.getFirstName());
    }

    @Test
    public void testSetFirstName() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).setFirstName(STRING_VALUE);
        }});
        hibernateAppUser.setFirstName(STRING_VALUE);
    }

    @Test
    public void testGetLastName() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).getLastName();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateAppUser.getLastName());
    }

    @Test
    public void testSetLastName() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).setLastName(STRING_VALUE);
        }});
        hibernateAppUser.setLastName(STRING_VALUE);
    }

    @Test
    public void testGetEmailAddress() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).getEmailAddress();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateAppUser.getEmailAddress());
    }

    @Test
    public void testSetEmailAddress() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).setEmailAddress(STRING_VALUE);
        }});
        hibernateAppUser.setEmailAddress(STRING_VALUE);
    }

    @Test
    public void testGetLastLogin() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).getLastLogout();
            will(returnValue(DATETIME_VALUE.withMillisOfSecond(0)));
        }});
        assertEquals(DATETIME_VALUE.withMillisOfSecond(0), hibernateAppUser.getLastLogout());
    }

    @Test
    public void testSetLastLogin() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).setLastLogout(DATETIME_VALUE.withMillisOfSecond(0));
        }});
        hibernateAppUser.setLastLogout(DATETIME_VALUE);
    }
}
