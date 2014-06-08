package com.jtbdevelopment.e_eye_o.security

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

/**
 * Date: 1/17/14
 * Time: 6:22 PM
 */
@Component("userDetailsService")
@SuppressWarnings("unused")
class UserDetailsServiceGImpl implements UserDetailsService {
    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private SecurityObjectFactory securityObjectFactory;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        AppUser appUser = readOnlyDAO.getUser(username);
        if (appUser) {
            return securityObjectFactory.newAppUserDetails(appUser);
        }
        throw new UsernameNotFoundException(username + " not found");
    }
}
