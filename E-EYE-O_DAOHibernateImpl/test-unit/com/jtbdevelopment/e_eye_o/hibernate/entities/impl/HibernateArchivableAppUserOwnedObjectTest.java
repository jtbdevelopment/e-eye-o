package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.hibernate.entities.wrapper.HibernateIdObjectWrapperFactory;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 1/21/13
 * Time: 12:22 PM
 */
public class HibernateArchivableAppUserOwnedObjectTest {
    public static interface LocalInterface extends ArchivableAppUserOwnedObject {
    }

    public static class HibernateLocal extends HibernateArchivableAppUserOwnedObject<LocalInterface> {
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
    public void testIsArchived() throws Exception {
        context.checking(new Expectations() {{
            one(impl).isArchived();
            will(returnValue(true));
        }});
        assertTrue(hibernateLocal.isArchived());
    }

    @Test
    public void testSetArchived() throws Exception {
        context.checking(new Expectations() {{
            one(impl).setArchived(false);
            will(returnValue(impl));
        }});
        assertSame(hibernateLocal, hibernateLocal.setArchived(false));
    }
}
