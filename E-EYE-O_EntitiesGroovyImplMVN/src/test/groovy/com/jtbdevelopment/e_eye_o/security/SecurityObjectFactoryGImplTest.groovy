package com.jtbdevelopment.e_eye_o.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserGImpl

/**
 * Date: 12/3/13
 * Time: 6:40 AM
 */
class SecurityObjectFactoryGImplTest extends AbstractSecurityObjectFactoryTest {

    @Override
    AppUser createAppUser() {
        return new AppUserGImpl()
    }

    @Override
    SecurityObjectFactory createFactory() {
        return new SecurityObjectFactoryGImpl()
    }

}
