package com.jtbdevelopment.e_eye_o.entities.impl.security

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails
import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Date: 12/2/13
 * Time: 11:27 PM
 */
@CompileStatic
class AppUserUserDetailsGImpl implements AppUserUserDetails {
    static protected List<GrantedAuthority> USER_ROLES = [
            new SimpleGrantedAuthority(AppUserUserDetails.ROLE_USER)].asImmutable()

    static protected List<GrantedAuthority> ADMIN_ROLES = [
            new SimpleGrantedAuthority(AppUserUserDetails.ROLE_USER),
            new SimpleGrantedAuthority(AppUserUserDetails.ROLE_ADMIN)].asImmutable()
    AppUser appUser;

    @Override
    Collection<? extends GrantedAuthority> getAuthorities() {
        return appUser.admin ? ADMIN_ROLES : USER_ROLES
    }

    @Override
    String getPassword() {
        return appUser.password
    }

    @Override
    String getUsername() {
        return appUser.emailAddress
    }

    @Override
    boolean isAccountNonExpired() {
        return appUser.active
    }

    @Override
    boolean isAccountNonLocked() {
        return appUser.activated
    }

    @Override
    boolean isCredentialsNonExpired() {
        return appUser.active
    }

    @Override
    boolean isEnabled() {
        return appUser.active
    }
}
