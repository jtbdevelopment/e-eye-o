package com.jtbdevelopment.e_eye_o.entities.impl.security;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.AppUserUserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Date: 4/5/13
 * Time: 11:57 PM
 */
class AppUserUserDetailsImpl implements AppUserUserDetails {

    //  TODO - clean up and maybe add more features
    private final AppUser appUser;

    AppUserUserDetailsImpl(final AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (appUser.isAdmin()) {
            return ADMIN_ROLES;
        } else {
            return USER_ROLES;
        }
    }

    @Override
    public String getPassword() {
        return appUser.getPassword();
    }

    @Override
    public String getUsername() {
        return appUser.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return appUser.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return appUser.isActivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return appUser.isActive();
    }

    @Override
    public boolean isEnabled() {
        return appUser.isActive();
    }

    public AppUser getAppUser() {
        return appUser;
    }
}
