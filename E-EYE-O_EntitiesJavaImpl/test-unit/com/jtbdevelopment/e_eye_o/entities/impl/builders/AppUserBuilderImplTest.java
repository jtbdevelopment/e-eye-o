package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

/**
 * Date: 3/9/13
 * Time: 11:46 AM
 */
public class AppUserBuilderImplTest extends IdObjectBuilderImplTest {
    final AppUser impl = factory.newAppUser();
    final AppUserBuilderImpl builder = new AppUserBuilderImpl(impl);

    @Test
    public void testWithFirstName() throws Exception {
        final String value = "first";
        assertEquals("", impl.getFirstName());
        assertSame(builder, builder.withFirstName(value));
        assertEquals(impl.getFirstName(), value);
    }

    @Test
    public void testWithLastName() throws Exception {
        final String value = "last";
        assertEquals("", impl.getLastName());
        assertSame(builder, builder.withLastName(value));
        assertEquals(impl.getLastName(), value);
    }

    @Test
    public void testWithEmailAddress() throws Exception {
        final String value = "email";
        assertEquals("", impl.getEmailAddress());
        assertSame(builder, builder.withEmailAddress(value));
        assertEquals(impl.getEmailAddress(), value);
    }

    @Test
    public void testWithLastLogin() throws Exception {
        final DateTime value = new DateTime();
        assertEquals(AppUser.NEVER_LOGGED_IN, impl.getLastLogout());
        assertSame(builder, builder.withLastLogout(value));
        assertEquals(impl.getLastLogout(), value);
    }

    @Test
    public void testWithAdmin() {
        assertFalse(impl.isAdmin());
        builder.withAdmin(true);
        assertTrue(impl.isAdmin());
    }

    @Test
    public void testWithActive() {
        assertTrue(impl.isActive());
        builder.withActive(false);
        assertFalse(impl.isActive());
    }

    @Test
    public void testWithActivated() {
        assertFalse(impl.isActivated());
        builder.withActivated(true);
        assertTrue(impl.isActivated());
    }

    @Test
    public void testPassword() {
        assertEquals("", impl.getPassword());
        String password = "gandalf";
        builder.withPassword(password);
        assertEquals(password, impl.getPassword());
    }
}
