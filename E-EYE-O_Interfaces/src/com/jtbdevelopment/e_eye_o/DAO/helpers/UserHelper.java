package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;

/**
 * Date: 4/6/13
 * Time: 6:25 PM
 */
public interface UserHelper {
    //  Take a basic login form and do whatever else we want to new user
    //  Implicitly records legal, cookie and privacy policy versions agreed to
    TwoPhaseActivity setUpNewUser(final AppUser appUser);

    TwoPhaseActivity generateActivationRequest(final AppUser appUser);

    TwoPhaseActivity requestResetPassword(final AppUser appUser);

    void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword);
}
