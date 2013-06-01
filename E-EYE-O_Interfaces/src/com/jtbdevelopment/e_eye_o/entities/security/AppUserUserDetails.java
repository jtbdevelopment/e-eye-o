package com.jtbdevelopment.e_eye_o.entities.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Date: 4/5/13
 * Time: 11:56 PM
 */
public interface AppUserUserDetails extends UserDetails {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    AppUser getAppUser();
}
