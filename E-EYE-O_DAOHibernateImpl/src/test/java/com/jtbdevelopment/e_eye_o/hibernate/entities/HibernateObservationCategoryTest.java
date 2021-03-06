package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/13/13
 * Time: 7:25 PM
 */
public class HibernateObservationCategoryTest extends HibernateAbstractIdObjectTest {
    private ObservationCategory implOC;
    private HibernateObservationCategory hibernateOC;
    private final String APP_USER_ID = "AUID";

    @BeforeMethod
    public void setUp() {
        super.setUp();
        implOC = context.mock(ObservationCategory.class, "default");
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
            oneOf(implOC).getShortName();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateOC.getShortName());
    }

    @Test
    public void testSetShortName() throws Exception {
        context.checking(new Expectations() {{
            oneOf(implOC).setShortName(STRING_VALUE);
        }});
        hibernateOC.setShortName(STRING_VALUE);
    }

    @Test
    public void testGetDescription() throws Exception {
        context.checking(new Expectations() {{
            oneOf(implOC).getDescription();
            will(returnValue(STRING_VALUE));
        }});
        assertEquals(STRING_VALUE, hibernateOC.getDescription());
    }

    @Test
    public void testSetDescription() throws Exception {
        context.checking(new Expectations() {{
            oneOf(implOC).setDescription(STRING_VALUE);
        }});
        hibernateOC.setDescription(STRING_VALUE);
    }
}
