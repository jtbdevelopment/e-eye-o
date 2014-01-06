package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import groovy.transform.CompileStatic
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**
 * Date: 12/14/13
 * Time: 8:22 PM
 */
@CompileStatic
@Component
class UserMaintenanceHelperGImpl implements UserMaintenanceHelper {
    @Autowired(required = false)
    PasswordEncoder passwordEncoder

    @Autowired
    ReadWriteDAO readWriteDAO

    @Autowired
    IdObjectFactory idObjectFactory

    @Override
    AppUserSettings getUserSettings(final AppUser appUser) {
        Set<AppUserSettings> entitiesForUser = readWriteDAO.getEntitiesForUser(AppUserSettings.class, appUser, 0, 0);
        if (entitiesForUser.empty) {
            return (AppUserSettings) readWriteDAO.create(idObjectFactory.newAppUserSettings(readWriteDAO.get(AppUser, appUser.id)))
        }
        entitiesForUser.iterator().next();
    }

    @Override
    boolean canChangeEmailAddress(final AppUser appUser) {
        return !hasRecentActivity(appUser, TwoPhaseActivity.Activity.PASSWORD_RESET);
    }

    @Override
    boolean canChangePassword(final AppUser appUser) {
        return !hasRecentActivity(appUser, TwoPhaseActivity.Activity.EMAIL_CHANGE);
    }

    @Override
    TwoPhaseActivity changeEmailAddress(final AppUser appUser, final String newEmailAddress) throws UserMaintenanceHelper.PasswordChangeTooRecent {
        if (!canChangeEmailAddress(appUser)) {
            throw new UserMaintenanceHelper.PasswordChangeTooRecent();
        }
        AppUser loadedUser = readWriteDAO.get(AppUser.class, appUser.getId());
        TwoPhaseActivity changeRequest = idObjectFactory.newTwoPhaseActivityBuilder(loadedUser).withActivityType(TwoPhaseActivity.Activity.EMAIL_CHANGE).withExpirationTime(new DateTime()).build();
        changeRequest = (TwoPhaseActivity) readWriteDAO.create(changeRequest);
        changeRequest.archived = true;
        loadedUser.emailAddress = newEmailAddress;
        readWriteDAO.trustedUpdates(Arrays.asList(loadedUser, changeRequest));
        return readWriteDAO.get(TwoPhaseActivity.class, changeRequest.getId());
    }

    @Override
    TwoPhaseActivity requestResetPassword(final AppUser appUser) throws UserMaintenanceHelper.EmailChangeTooRecent {
        if (!canChangePassword(appUser)) {
            throw new UserMaintenanceHelper.EmailChangeTooRecent();
        }
        //  TODO - configurable
        TwoPhaseActivity requestReset = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.PASSWORD_RESET).withExpirationTime(new DateTime().plusDays(1)).build();
        (TwoPhaseActivity) readWriteDAO.create(requestReset);
    }

    @Override
    void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        AppUser appUser = readWriteDAO.get(AppUser.class, twoPhaseActivity.getAppUser().getId());
        appUser.password = encryptPassword(newPassword);
        twoPhaseActivity.archived = true;
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, twoPhaseActivity));
    }

    @Override
    AppUserSettings updateSettings(final AppUser appUser, final Map<String, Object> settings) {
        AppUserSettings userSettings = getUserSettings(appUser)
        userSettings.updateSettings(settings);
        return (AppUserSettings) readWriteDAO.trustedUpdate(userSettings);
    }

    private String encryptPassword(final String password) {
        if (passwordEncoder) {
            return passwordEncoder.encode(password)
        }
        return password
    }

    private boolean hasRecentActivity(final AppUser appUser, final TwoPhaseActivity.Activity activityToCheckFor) {
        //  TODO - make configurable
        final DateTime tooRecent = DateTime.now().minusDays(7);
        Set<TwoPhaseActivity> activities = readWriteDAO.getEntitiesForUser(TwoPhaseActivity.class, appUser, 0, 0);
        activities.find({ TwoPhaseActivity activity -> activity.activityType == activityToCheckFor && activity.modificationTimestamp.compareTo(tooRecent) > 0 }) != null
    }
}
