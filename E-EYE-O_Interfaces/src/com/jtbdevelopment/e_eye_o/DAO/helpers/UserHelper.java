package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;

/**
 * Date: 4/6/13
 * Time: 6:25 PM
 */
public interface UserHelper {
    public class EmailChangeTooRecent extends Exception {
    }

    public class PasswordChangeTooRecent extends Exception {
    }


    /**
     * Take new user shell and create
     * Secure password if a password securer is defined
     * Stored policy agreements on terms, privacy and cookies if defined
     * Call over to new user helper if defined
     *
     * @param appUser new app user
     * @return two phase activity to activate user
     */
    TwoPhaseActivity createNewUser(final AppUser appUser);

    TwoPhaseActivity generateActivationRequest(final AppUser appUser);

    boolean canChangeEmailAddress(final AppUser appUser);

    boolean canChangePassword(final AppUser appUser);

    TwoPhaseActivity changeEmailAddress(final AppUser appUser, final String newEmailAddress) throws PasswordChangeTooRecent;

    TwoPhaseActivity requestResetPassword(final AppUser appUser) throws EmailChangeTooRecent;

    void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword);
}
