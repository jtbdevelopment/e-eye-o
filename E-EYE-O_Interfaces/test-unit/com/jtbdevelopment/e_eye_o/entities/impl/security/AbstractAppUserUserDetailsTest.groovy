package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/2/13
 * Time: 11:34 PM
 */
abstract class AbstractAppUserUserDetailsTest {
    AppUser appUser
    AppUserUserDetails details

    abstract AppUser createAppUser(final String email, final String password, boolean activated, boolean admin, boolean active);

    abstract AppUserUserDetails createAppUserDetails(final AppUser user);

    @BeforeMethod
    public void setUp() {
        appUser = createAppUser("email", "pass", false, false, true)
        details = createAppUserDetails(appUser)
    }

    @Test
    void testGetAuthorities() {
        assert AppUserUserDetails.USER_ROLES.is(details.authorities)
        appUser.admin = true
        assert AppUserUserDetails.ADMIN_ROLES.is(details.authorities)
    }

    @Test
    void testGetPassword() {
        assert appUser.password == details.password
    }

    @Test
    void testGetUsername() {
        assert appUser.emailAddress == details.username
    }

    @Test
    void testIsAccountNonExpired() {
        assert appUser.active == details.accountNonExpired
        appUser.active = !appUser.active
        assert appUser.active == details.accountNonExpired
    }

    @Test
    void testIsAccountNonLocked() {
        assert appUser.activated == details.accountNonLocked
        appUser.activated = !appUser.activated
        assert appUser.activated == details.accountNonLocked
    }

    @Test
    void testIsCredentialsNonExpired() {
        assert appUser.active == details.credentialsNonExpired
        appUser.active = !appUser.active
        assert appUser.active == details.credentialsNonExpired
    }

    @Test
    void testIsEnabled() {
        assert appUser.active == details.enabled
        appUser.active = !appUser.active
        assert appUser.active == details.enabled
    }
}
