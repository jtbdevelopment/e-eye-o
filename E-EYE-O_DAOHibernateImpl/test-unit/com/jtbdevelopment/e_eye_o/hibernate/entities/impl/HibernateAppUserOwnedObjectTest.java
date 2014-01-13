package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.wrapper.IdObjectWrapperFactory;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 1/21/13
 * Time: 11:54 AM
 */
public class HibernateAppUserOwnedObjectTest extends HibernateAbstractIdObjectTest {
    public static interface LocalInterface extends AppUserOwnedObject {
    }

    public static class HibernateLocal extends HibernateAppUserOwnedObject<LocalInterface> {
        public HibernateLocal(final LocalInterface localInterface) {
            super(localInterface);
        }
    }

    private LocalInterface impl;
    private HibernateLocal hibernateLocal;

    @BeforeMethod
    public void setUp() {
        super.setUp();
        impl = context.mock(LocalInterface.class, "default");
        hibernateLocal = new HibernateLocal(impl);
        context.checking(new Expectations() {{
            allowing(implFactory).newIdObject(Photo.class);
            will(returnValue(impl));
        }});
    }

    @Test
    public void testGetAppUser() throws Exception {
        final AppUser au = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            oneOf(impl).getAppUser();
            will(returnValue(au));
        }});

        assertSame(au, hibernateLocal.getAppUser());
    }

    @Test
    public void testSetAppUser() throws Exception {
        final AppUser au = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            oneOf(impl).setAppUser(au);
            oneOf(idObjectWrapperFactory).wrap(IdObjectWrapperFactory.WrapperKind.DAO, au);
            will(returnValue(au));
        }});

        hibernateLocal.setAppUser(au);
    }

    @Test
    public void testIsArchived() throws Exception {
        context.checking(new Expectations() {{
            oneOf(impl).isArchived();
            will(returnValue(true));
        }});
        assertTrue(hibernateLocal.isArchived());
    }

    @Test
    public void testSetArchived() throws Exception {
        context.checking(new Expectations() {{
            oneOf(impl).setArchived(false);
        }});
        hibernateLocal.setArchived(false);
    }
}
