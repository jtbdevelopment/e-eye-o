package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails

/**
 * Date: 12/2/13
 * Time: 11:34 PM
 */
class AppUserUserDetailsGImplTest extends AbstractAppUserUserDetailsTest {
    @Override
    AppUser createAppUser(final String email, final String password, final boolean activated, final boolean admin, final boolean active) {
        return new AppUserGImpl(activated: activated, active: active, password: password, emailAddress: email, admin: admin)
    }

    @Override
    AppUserUserDetails createAppUserDetails(final AppUser user) {
        return new AppUserUserDetailsGImpl(appUser: user)
    }

}
