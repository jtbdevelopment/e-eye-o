package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/13/13
 * Time: 7:55 PM
 */
public class HibernateAppUserTest {
    private Mockery context;
    private IdObjectFactory implFactory;
    private AppUser implAppUser;
    private HibernateAppUser hibernateAppUser;
    private HibernateIdObjectWrapperFactory daoFactory;
    private final DateTime DATE_VALUE = new DateTime();
    private final String APP_USER_ID = "AUID";
    private final String STRING_VALUE = "S";

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        implAppUser = context.mock(AppUser.class, "default");
        daoFactory = new HibernateIdObjectWrapperFactory(implFactory);
        hibernateAppUser = new HibernateAppUser(implAppUser);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(AppUser.class);
            will(returnValue(implAppUser));
            allowing(implAppUser).getId();
            will(returnValue(APP_USER_ID));
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
            will(returnValue(implAppUser));
        }});
        assertSame(hibernateAppUser, hibernateAppUser.setFirstName(STRING_VALUE));
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
            will(returnValue(implAppUser));
        }});
        assertSame(hibernateAppUser, hibernateAppUser.setLastName(STRING_VALUE));
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
            will(returnValue(implAppUser));
        }});
        assertSame(hibernateAppUser, hibernateAppUser.setEmailAddress(STRING_VALUE));
    }

    @Test
    public void testGetLastLogin() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).getLastLogin();
            will(returnValue(DATE_VALUE));
        }});
        assertEquals(DATE_VALUE, hibernateAppUser.getLastLogin());
    }

    @Test
    public void testSetLastLogin() throws Exception {
        context.checking(new Expectations() {{
            one(implAppUser).setLastLogin(DATE_VALUE);
            will(returnValue(implAppUser));
        }});
        assertSame(hibernateAppUser, hibernateAppUser.setLastLogin(DATE_VALUE));
    }
}
