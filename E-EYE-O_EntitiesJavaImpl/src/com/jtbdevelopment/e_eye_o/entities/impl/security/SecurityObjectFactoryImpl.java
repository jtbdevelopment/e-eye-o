package com.jtbdevelopment.e_eye_o.entities.impl.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import com.jtbdevelopment.e_eye_o.entities.security.SecurityObjectFactory;
import org.springframework.stereotype.Component;

/**
 * Date: 4/6/13
 * Time: 12:01 AM
 */
@Component
@SuppressWarnings("unused")
public class SecurityObjectFactoryImpl implements SecurityObjectFactory {
    @Override
    public AppUserUserDetails newAppUserDetails(final AppUser appUser) {
        return new AppUserUserDetailsImpl(appUser);
    }
}
