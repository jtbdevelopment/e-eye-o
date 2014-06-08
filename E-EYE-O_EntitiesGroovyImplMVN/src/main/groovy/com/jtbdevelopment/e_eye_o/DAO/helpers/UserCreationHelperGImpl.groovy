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
class UserCreationHelperGImpl implements UserCreationHelper {
    @Autowired(required = false)
    PasswordEncoder passwordEncoder

    @Autowired(required = false)
    UserNewUserDefaultsCreator userNewUserDefaultsCreator

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
    AppUser activateUser(final TwoPhaseActivity activity) {
        AppUser appUser = readWriteDAO.get(AppUser.class, activity.getAppUser().getId());
        appUser.active = true;
        appUser.activated = true;
        activity.archived = true;
        activity.appUser = appUser
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, activity));
        return readWriteDAO.get(AppUser.class, appUser.getId());
    }

    private String encryptPassword(final String password) {
        if (passwordEncoder) {
            return passwordEncoder.encode(password)
        }
        return password
    }

    private void newUserCustomizationIfPossible(final AppUser appUser) {
        if (userNewUserDefaultsCreator != null) {
            userNewUserDefaultsCreator.initializeNewUser(appUser);
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
