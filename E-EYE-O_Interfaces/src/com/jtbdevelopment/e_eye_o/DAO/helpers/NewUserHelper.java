package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;

/**
 * Date: 4/6/13
 * Time: 6:25 PM
 */
public interface NewUserHelper {
    //  Take a basic login form and do whatever else we want to new user
    TwoPhaseActivity setUpNewUser(final AppUser appUser);
}
