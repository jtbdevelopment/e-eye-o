package com.jtbdevelopment.e_eye_o.entities.impl.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.testng.AssertJUnit.*;

/**
 * Date: 5/31/13
 * Time: 6:51 AM
 */
public class AppUserUserDetailsImplTest {
    private Mockery context;
    private AppUser appUser;
    private AppUserUserDetailsImpl details;

    @BeforeMethod
    public void setUp() {
        context = new Mockery();
        appUser = context.mock(AppUser.class);
        details = new AppUserUserDetailsImpl(appUser);
    }

    @Test
    public void testGetAuthoritiesNonAdmin() throws Exception {
        context.checking(new Expectations() {{
            one(appUser).isAdmin();
            will(returnValue(false));
        }});
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals(AppUserUserDetailsImpl.ROLE_USER, authorities.iterator().next().getAuthority());
    }

    @Test
    public void testGetAuthoritiesAdmin() throws Exception {
        context.checking(new Expectations() {{
            one(appUser).isAdmin();
            will(returnValue(true));
        }});
        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(AppUserUserDetails.ROLE_ADMIN)));
        assertTrue(authorities.contains(new SimpleGrantedAuthority(AppUserUserDetails.ROLE_USER)));
    }

    @Test
    public void testGetPassword() throws Exception {
        final String password = "PASS!";
        context.checking(new Expectations() {{
            one(appUser).getPassword();
            will(returnValue(password));
        }});
        assertEquals(password, details.getPassword());
    }

    @Test
    public void testGetUsername() throws Exception {
        final String email = "email@email";
        context.checking(new Expectations() {{
            one(appUser).getEmailAddress();
            will(returnValue(email));
        }});
        assertEquals(email, details.getUsername());
    }

    @Test
    public void testIsAccountDrivenByActiveWhenActiveTrue() throws Exception {
        testFunctionsDrivenByIsActive(true);
    }

    @Test
    public void testIsAccountDrivenByActiveWhenActiveFalse() throws Exception {
        testFunctionsDrivenByIsActive(false);
    }

    private void testFunctionsDrivenByIsActive(final boolean active) {
        context.checking(new Expectations() {{
            allowing(appUser).isActive();
            will(returnValue(active));
        }});
        assertEquals(active, details.isEnabled());
        assertEquals(active, details.isAccountNonExpired());
        assertEquals(active, details.isCredentialsNonExpired());
    }

    @Test
    public void testNonLockedWhenActivated() {
        context.checking(new Expectations() {{
            allowing(appUser).isActivated();
            will(returnValue(true));
        }});
        assertTrue(details.isAccountNonLocked());
    }

    @Test
    public void testNonLockedWhenNotActivated() {
        context.checking(new Expectations() {{
            allowing(appUser).isActivated();
            will(returnValue(false));
        }});
        assertTrue(!details.isAccountNonLocked());
    }

    @Test
    public void testGetAppUser() {
        assertSame(appUser, details.getAppUser());
    }
}
