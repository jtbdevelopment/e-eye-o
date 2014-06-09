package com.jtbdevelopment.e_eye_o.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl

/**
 * Date: 12/3/13
 * Time: 6:40 AM
 */
class SecurityObjectFactoryImplTest extends AbstractSecurityObjectFactoryTest {

    @Override
    AppUser createAppUser() {
        return new AppUserImpl()
    }

    @Override
    SecurityObjectFactory createFactory() {
        return new SecurityObjectFactoryImpl()
    }

}
