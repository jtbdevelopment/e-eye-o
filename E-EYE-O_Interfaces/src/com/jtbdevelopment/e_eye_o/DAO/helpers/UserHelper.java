package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
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

    //  Take a basic login form and do whatever else we want to new user
    //  Implicitly records legal, cookie and privacy policy versions agreed to
    TwoPhaseActivity setUpNewUser(final AppUser appUser);

    AppUserSettings createDefaultSettingsForNewUser(final AppUser newUser);

    TwoPhaseActivity generateActivationRequest(final AppUser appUser);

    boolean canChangeEmailAddress(final AppUser appUser);

    boolean canChangePassword(final AppUser appUser);

    TwoPhaseActivity changeEmailAddress(final AppUser appUser, final String newEmailAddress) throws PasswordChangeTooRecent;

    TwoPhaseActivity requestResetPassword(final AppUser appUser) throws EmailChangeTooRecent;

    void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword);
}
