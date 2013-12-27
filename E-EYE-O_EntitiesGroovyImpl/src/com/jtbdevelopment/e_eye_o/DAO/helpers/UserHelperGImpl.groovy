package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import com.jtbdevelopment.e_eye_o.helpandlegal.CookiesPolicy
import com.jtbdevelopment.e_eye_o.helpandlegal.PrivacyPolicy
import com.jtbdevelopment.e_eye_o.helpandlegal.TermsAndConditions
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
class UserHelperGImpl implements UserHelper {
    @Autowired(required = false)
    PasswordEncoder passwordEncoder

    @Autowired(required = false)
    NewUserHelper newUserHelper

    @Autowired(required = false)
    TermsAndConditions termsAndConditions

    @Autowired(required = false)
    CookiesPolicy cookiesPolicy

    @Autowired(required = false)
    PrivacyPolicy privacyPolicy

    @Autowired
    ReadWriteDAO readWriteDAO

    @Autowired
    IdObjectFactory idObjectFactory

    @Override
    TwoPhaseActivity createNewUser(final AppUser appUser) {
        appUser.password = encryptPassword(appUser.getPassword());
        AppUser savedUser = (AppUser) readWriteDAO.create(appUser);
        recordUserPolicyAgreementsIfPossible(savedUser);
        newUserCustomizationIfPossible(savedUser)
        return generateActivationRequest(savedUser);
    }

    @Override
    TwoPhaseActivity generateActivationRequest(final AppUser appUser) {
        //  TODO - configurable time
        return (TwoPhaseActivity) readWriteDAO.create(
                idObjectFactory.newTwoPhaseActivityBuilder(appUser).
                        withActivityType(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION).
                        withExpirationTime(new DateTime().plusDays(1)).
                        build());
    }

    @Override
    AppUser activateUser(final TwoPhaseActivity appUser) {
        return null
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
    TwoPhaseActivity changeEmailAddress(final AppUser appUser, final String newEmailAddress) throws UserHelper.PasswordChangeTooRecent {
        if (!canChangeEmailAddress(appUser)) {
            throw new UserHelper.PasswordChangeTooRecent();
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
    TwoPhaseActivity requestResetPassword(final AppUser appUser) throws UserHelper.EmailChangeTooRecent {
        if (!canChangePassword(appUser)) {
            throw new UserHelper.EmailChangeTooRecent();
        }
        //  TODO - configurable
        TwoPhaseActivity requestReset = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.PASSWORD_RESET).withExpirationTime(new DateTime().plusDays(1)).build();
        (TwoPhaseActivity) readWriteDAO.create(requestReset);
    }

    @Override
    void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        AppUser appUser = readWriteDAO.get(AppUser.class, twoPhaseActivity.getAppUser().getId());
        appUser.password = newPassword;
        twoPhaseActivity.archived = true;
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, twoPhaseActivity));
    }

    @Override
    void deactivateUser(final AppUser user) {
        AppUser loaded = readWriteDAO.get(AppUser.class, user.getId());
        loaded.active = false;
        readWriteDAO.trustedUpdate(loaded);
    }

    @Override
    AppUserSettings updateSettings(final AppUser appUser, final Map<String, Object> settings) {
        Set<AppUserSettings> entitiesForUser = readWriteDAO.getEntitiesForUser(AppUserSettings.class, appUser, 0, 0);
        if (entitiesForUser.size() != 1) {
            throw new IllegalStateException("Somehow more than 1 app settings");
        }
        AppUserSettings userSettings = entitiesForUser.iterator().next();
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

    private void newUserCustomizationIfPossible(final AppUser appUser) {
        if (newUserHelper != null) {
            newUserHelper.initializeNewUser(appUser);
        }
    }

    private AppUserSettings recordUserPolicyAgreementsIfPossible(final AppUser newUser) {
        DateTime now = DateTime.now();
        return (AppUserSettings) readWriteDAO.create(
                idObjectFactory.newAppUserSettingsBuilder(newUser).
                        withSetting(AppUserSettings.COOKIES_POLICY_VERSION, cookiesPolicy == null ? 0 : cookiesPolicy.getVersion()).
                        withSetting(AppUserSettings.COOKIES_POLICY_TIMESTAMP, now.getMillis()).
                        withSetting(AppUserSettings.PRIVACY_POLICY_VERSION, privacyPolicy == null ? 0 : privacyPolicy.getVersion()).
                        withSetting(AppUserSettings.PRIVACY_POLICY_TIMESTAMP, now.getMillis()).
                        withSetting(AppUserSettings.TERMS_AND_CONDITIONS_VERSION, termsAndConditions == null ? 0 : termsAndConditions.getVersion()).
                        withSetting(AppUserSettings.TERMS_AND_CONDITIONS_TIMESTAMP, now.getMillis()).
                        build()
        );
    }
}
