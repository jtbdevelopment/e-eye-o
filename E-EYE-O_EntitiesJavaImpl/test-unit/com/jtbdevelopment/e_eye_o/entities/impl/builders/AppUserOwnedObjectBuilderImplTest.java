package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

/**
 * Date: 3/9/13
 * Time: 11:54 AM
 */
public class AppUserOwnedObjectBuilderImplTest extends IdObjectBuilderImplTest {
    public interface LocalEntity extends AppUserOwnedObject {
    }

    public class LocalEntityImpl extends AppUserOwnedObjectImpl implements LocalEntity {
        public LocalEntityImpl() {
            super(null);
        }
    }

    final protected AppUser appUser = factory.newAppUser();
    final LocalEntityImpl impl = new LocalEntityImpl();
    final AppUserOwnedObjectBuilderImpl<LocalEntity> builder = new AppUserOwnedObjectBuilderImpl<LocalEntity>(impl);

    @Test
    public void testWithAppUser() throws Exception {
        assertNull(impl.getAppUser());
        assertSame(builder, builder.withAppUser(appUser));
        assertSame(appUser, impl.getAppUser());
    }

    @Test
    public void testWithArchiveFlag() throws Exception {
        assertFalse(impl.isArchived());
        assertSame(builder, builder.withArchiveFlag(true));
        assertTrue(impl.isArchived());
    }
}
