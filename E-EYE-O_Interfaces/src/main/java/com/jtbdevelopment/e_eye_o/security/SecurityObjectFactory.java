package com.jtbdevelopment.e_eye_o.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

/**
 * Date: 4/6/13
 * Time: 12:01 AM
 */
public interface SecurityObjectFactory {
    AppUserUserDetails newAppUserDetails(final AppUser appUser);
}
