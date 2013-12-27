package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;

import java.util.Map;

/**
 * Date: 12/26/13
 * Time: 10:57 PM
 */
public interface UserMaintenanceHelper {
    boolean canChangeEmailAddress(AppUser appUser);

    boolean canChangePassword(AppUser appUser);

    TwoPhaseActivity changeEmailAddress(AppUser appUser, String newEmailAddress) throws PasswordChangeTooRecent;

    TwoPhaseActivity requestResetPassword(AppUser appUser) throws EmailChangeTooRecent;

    void resetPassword(TwoPhaseActivity twoPhaseActivity, String newPassword);

    AppUserSettings updateSettings(AppUser appUser, Map<String, Object> settings);

    public class EmailChangeTooRecent extends Exception {
    }

    public class PasswordChangeTooRecent extends Exception {
    }
}
