package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.security.SecurityObjectFactory
import org.testng.annotations.Test

/**
 * Date: 12/3/13
 * Time: 6:40 AM
 */
abstract class AbstractSecurityObjectFactoryTest {

    abstract AppUser createAppUser();

    abstract SecurityObjectFactory createFactory();

    @Test
    void testNewAppUserDetails() {
        AppUser user = createAppUser()
        SecurityObjectFactory impl = createFactory()
        assert user.is(impl.newAppUserDetails(user).appUser)
    }
}
