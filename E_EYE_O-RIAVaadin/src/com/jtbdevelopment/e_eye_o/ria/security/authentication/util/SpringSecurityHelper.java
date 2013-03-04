package com.jtbdevelopment.e_eye_o.ria.security.authentication.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Date: 3/3/13
 * Time: 11:35 AM
 */
public class SpringSecurityHelper {
    public static boolean hasRole(String role) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
}
