package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/13/13
 * Time: 7:25 PM
 */
public class HibernateObservationCategoryTest {
    private Mockery context;
    private IdObjectFactory implFactory;
    private ObservationCategory implOC;
    private HibernateObservationCategory hibernateOC;
    private final String APP_USER_ID = "AUID";
    private final String STRING_VALUE = "S";

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        implOC = context.mock(ObservationCategory.class, "default");
        @SuppressWarnings("unused")
        HibernateIdObjectWrapperFactory daoFactory = new HibernateIdObjectWrapperFactory(implFactory);
        hibernateOC = new HibernateObservationCategory(implOC);
        final AppUser appUser = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(ObservationCategory.class);
            will(returnValue(implOC));
            allowing(implOC).getAppUser();
            will(returnValue(appUser));
            allowing(appUser).getId();
            will(returnValue(APP_USER_ID));
        }});
    }

    @Test
    public void testDefaultConstructor() {
        assertSame(implOC, new HibernateObservationCategory().getWrapped());
    }

    @Test
    public void testWrappedConstructor() {
        final ObservationCategory local = context.mock(ObservationCategory.class, "local");
        assertSame(local, new HibernateObservationCategory(local).getWrapped());
    }

    @Test
    public void testWrappedConstructorFromWrapped() {
        final ObservationCategory local = context.mock(ObservationCategory.class, "local");
        assertSame(local, new HibernateObservationCategory(new HibernateObservationCategory(local)).getWrapped());
    }


    @Test
    public void testGetAppUserID() throws Exception {
        assertEquals(APP_USER_ID, hibernateOC.getAppUserID());
    }

    @Test
    public void testSetAppUserIDDoesNothing() throws Exception {
        hibernateOC.setAppUserID("!" + APP_USER_ID);
        assertEquals(APP_USER_ID, hibernateOC.getAppUserID());
    }

    @Test
    public void testGetShortName() throws Exception {
        context.checking(new Expectations() {{
            one(implOC).getShortName();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateOC.getShortName());
    }

    @Test
    public void testSetShortName() throws Exception {
        context.checking(new Expectations() {{
            one(implOC).setShortName(STRING_VALUE);
            will(returnValue(implOC));
        }});
        assertSame(hibernateOC, hibernateOC.setShortName(STRING_VALUE));
    }

    @Test
    public void testGetDescription() throws Exception {
        context.checking(new Expectations() {{
            one(implOC).getDescription();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateOC.getDescription());
    }

    @Test
    public void testSetDescription() throws Exception {
        context.checking(new Expectations() {{
            one(implOC).setDescription(STRING_VALUE);
            will(returnValue(implOC));
        }});
        assertSame(hibernateOC, hibernateOC.setDescription(STRING_VALUE));
    }
}
