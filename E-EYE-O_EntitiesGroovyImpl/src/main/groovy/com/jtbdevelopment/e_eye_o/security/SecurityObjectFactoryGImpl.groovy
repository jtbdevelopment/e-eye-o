package com.jtbdevelopment.e_eye_o.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

/**
 * Date: 12/3/13
 * Time: 6:37 AM
 */
@Service
@CompileStatic
class SecurityObjectFactoryGImpl implements SecurityObjectFactory {
    @Override
    AppUserUserDetails newAppUserDetails(final AppUser appUser) {
        return new AppUserUserDetailsGImpl(appUser: appUser)
    }
}
