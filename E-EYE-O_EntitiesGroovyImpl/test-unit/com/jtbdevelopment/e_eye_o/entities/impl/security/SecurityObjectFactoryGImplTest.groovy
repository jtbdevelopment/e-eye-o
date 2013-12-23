package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl
import com.jtbdevelopment.e_eye_o.entities.security.SecurityObjectFactory

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
