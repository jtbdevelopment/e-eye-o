package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;

/**
 * Date: 12/10/13
 * Time: 8:24 PM
 * <p/>
 * This is an optional interface.  If you want to create any objects for new users this is where to do it
 */
public interface NewUserHelper {

    void initializeNewUser(final AppUser newUser);
}
