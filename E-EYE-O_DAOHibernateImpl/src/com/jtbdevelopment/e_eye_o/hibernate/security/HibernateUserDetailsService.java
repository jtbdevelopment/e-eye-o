package com.jtbdevelopment.e_eye_o.hibernate.security;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.security.SecurityObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Date: 4/5/13
 * Time: 10:29 PM
 */
@SuppressWarnings("unused")
public class HibernateUserDetailsService implements UserDetailsService {
    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private SecurityObjectFactory securityObjectFactory;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        AppUser appUser = readOnlyDAO.getUser(username);
        if (appUser != null) {
            return securityObjectFactory.newAppUserDetails(appUser);
        }
        throw new UsernameNotFoundException(username + " not found");
    }

}
