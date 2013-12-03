package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.impl.AppUserGImpl
import org.testng.annotations.Test

/**
 * Date: 12/3/13
 * Time: 6:40 AM
 */
class SecurityObjectFactoryGImplTest extends GroovyTestCase {

    @Test
    void testNewAppUserDetails() {
        AppUserGImpl user = new AppUserGImpl()
        SecurityObjectFactoryGImpl impl = new SecurityObjectFactoryGImpl()
        assert user.is(impl.newAppUserDetails(user).appUser)
    }
}
