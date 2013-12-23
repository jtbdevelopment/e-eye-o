package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserImpl
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails

/**
 * Date: 12/2/13
 * Time: 11:34 PM
 */
class AppUserUserDetailsImplTest extends AbstractAppUserUserDetailsTest {
    @Override
    AppUser createAppUser(final String email, final String password, final boolean activated, final boolean admin, final boolean active) {
        AppUserImpl impl = new AppUserImpl()
        impl.setPassword(password)
        impl.activated = activated
        impl.active = active
        impl.emailAddress = email
        impl.admin = admin
        return impl
    }

    @Override
    AppUserUserDetails createAppUserDetails(final AppUser user) {
        return new AppUserUserDetailsImpl(user)
    }

}
