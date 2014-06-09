package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;

/**
 * Date: 12/26/13
 * Time: 10:34 PM
 */
public interface UserCreationHelper {
    /**
     * Take new user shell and create
     * Secure password if a password securer is defined
     * Stored policy agreements on terms, privacy and cookies if defined
     * Call over to new user helper if defined
     *
     * @param appUser new app user
     * @return two phase activity to activate user
     */
    TwoPhaseActivity createNewUser(AppUser appUser);

    TwoPhaseActivity generateActivationRequest(AppUser appUser);

    AppUser activateUser(TwoPhaseActivity appUser);
}
