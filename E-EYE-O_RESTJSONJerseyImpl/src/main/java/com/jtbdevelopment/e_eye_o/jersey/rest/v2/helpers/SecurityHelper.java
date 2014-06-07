package com.jtbdevelopment.e_eye_o.jersey.rest.v2.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

/**
 * Date: 10/2/13
 * Time: 8:55 PM
 */
public interface SecurityHelper {
    AppUser getSessionAppUser() throws AuthenticationCredentialsNotFoundException;
}
