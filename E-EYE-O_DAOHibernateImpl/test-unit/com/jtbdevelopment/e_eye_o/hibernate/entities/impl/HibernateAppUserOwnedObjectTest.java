package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/21/13
 * Time: 11:54 AM
 */
public class HibernateAppUserOwnedObjectTest {
    public static interface LocalInterface extends AppUserOwnedObject {
    }

    public static class HibernateLocal extends HibernateAppUserOwnedObject<LocalInterface> {
        public HibernateLocal(final LocalInterface localInterface) {
            super(localInterface);
        }
    }

    private Mockery context;
    private IdObjectFactory implFactory;
    private LocalInterface impl;
    private HibernateLocal hibernateLocal;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        implFactory = context.mock(IdObjectFactory.class);
        impl = context.mock(LocalInterface.class, "default");
        @SuppressWarnings("unused")
        HibernateIdObjectWrapperFactory daoFactory = new HibernateIdObjectWrapperFactory(implFactory);
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
            one(impl).getAppUser();
            will(returnValue(au));
        }});

        assertSame(au, hibernateLocal.getAppUser());
    }

    @Test
    public void testSetAppUser() throws Exception {
        final AppUser au = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            one(impl).setAppUser(with(any(HibernateAppUser.class)));
            will(returnValue(impl));
        }});

        assertSame(hibernateLocal, hibernateLocal.setAppUser(au));
    }
}
