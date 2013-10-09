package com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Date: 5/14/13
 * Time: 7:05 AM
 */

@Component
@SuppressWarnings("unused")
public class SecurityHelperImpl implements SecurityHelper {
    @Override
    public AppUser getSessionAppUser() throws AuthenticationCredentialsNotFoundException {
        final Object principalAsObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalAsObject instanceof AppUserUserDetails) {
            return ((AppUserUserDetails) principalAsObject).getAppUser();
        } else {
            throw new AuthenticationCredentialsNotFoundException("No user for session");
        }
    }
}
