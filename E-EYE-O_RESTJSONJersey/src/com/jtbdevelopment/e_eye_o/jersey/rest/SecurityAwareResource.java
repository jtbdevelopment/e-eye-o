package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Date: 5/14/13
 * Time: 7:05 AM
 */

public abstract class SecurityAwareResource {
    protected AppUser getSessionAppUser() {
        final Object principalAsObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalAsObject instanceof AppUserUserDetails) {
            return ((AppUserUserDetails) principalAsObject).getAppUser();
        } else {
            return null;
        }
    }
}
