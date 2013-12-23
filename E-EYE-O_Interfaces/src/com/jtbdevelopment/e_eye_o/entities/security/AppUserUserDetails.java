package com.jtbdevelopment.e_eye_o.entities.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Date: 4/5/13
 * Time: 11:56 PM
 */
public interface AppUserUserDetails extends UserDetails {
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public List<GrantedAuthority> USER_ROLES = Collections.unmodifiableList(
            Arrays.<GrantedAuthority>asList(
                    new SimpleGrantedAuthority(ROLE_USER)));

    public List<GrantedAuthority> ADMIN_ROLES = Collections.unmodifiableList(
            Arrays.<GrantedAuthority>asList(
                    new SimpleGrantedAuthority(ROLE_USER),
                    new SimpleGrantedAuthority(ROLE_ADMIN)));


    AppUser getAppUser();
}
