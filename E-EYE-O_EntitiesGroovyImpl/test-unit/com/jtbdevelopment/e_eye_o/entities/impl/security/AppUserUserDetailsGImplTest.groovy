package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/2/13
 * Time: 11:34 PM
 */
class AppUserUserDetailsGImplTest {
    final AppUser appUser = new AppUserGImpl(activated: false, active: true, password: "pass", emailAddress: "email", admin: false)
    final AppUserUserDetailsGImpl details = new AppUserUserDetailsGImpl(appUser: appUser)

    @BeforeMethod
    public void setUp() {

    }

    @Test
    void testGetAuthorities() {
        assert AppUserUserDetailsGImpl.USER_ROLES.is(details.authorities)
        appUser.admin = true
        assert AppUserUserDetailsGImpl.ADMIN_ROLES.is(details.authorities)
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
