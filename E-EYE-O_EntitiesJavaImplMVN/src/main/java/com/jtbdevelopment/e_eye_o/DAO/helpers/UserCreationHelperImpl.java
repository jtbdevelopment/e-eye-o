package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.helpandlegal.CookiesPolicy;
import com.jtbdevelopment.e_eye_o.helpandlegal.PrivacyPolicy;
import com.jtbdevelopment.e_eye_o.helpandlegal.TermsAndConditions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Date: 4/6/13
 * Time: 6:26 PM
 * <p/>
 */
@Component
public class UserCreationHelperImpl implements UserCreationHelper {
    @Autowired(required = false)
    private TermsAndConditions termsAndConditions;

    @Autowired(required = false)
    private PrivacyPolicy privacyPolicy;

    @Autowired(required = false)
    private CookiesPolicy cookiesPolicy;

    @Autowired(required = false)
    protected UserNewUserDefaultsCreator userNewUserDefaultsCreator;

    @Autowired(required = false)
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ReadWriteDAO readWriteDAO;

    @Autowired
    protected IdObjectFactory idObjectFactory;

    @Override
    public TwoPhaseActivity createNewUser(final AppUser appUser) {
        appUser.setPassword(securePassword(appUser.getPassword()));
        AppUser savedUser = readWriteDAO.create(appUser);
        recordUserPolicyAgreementsIfPossible(savedUser);
        if (userNewUserDefaultsCreator != null) {
            userNewUserDefaultsCreator.initializeNewUser(savedUser);
        }
        return generateActivationRequest(savedUser);
    }

    @Override
    public TwoPhaseActivity generateActivationRequest(final AppUser appUser) {
        //  TODO - configurable
        return readWriteDAO.create(idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION).withExpirationTime(new DateTime().plusDays(1)).build());
    }

    @Override
    public AppUser activateUser(final TwoPhaseActivity activity) {
        AppUser appUser = readWriteDAO.get(AppUser.class, activity.getAppUser().getId());
        appUser.setActive(true);
        appUser.setActivated(true);
        activity.setArchived(true);
        activity.setAppUser(appUser);
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, activity));
        return readWriteDAO.get(AppUser.class, appUser.getId());
    }

    private AppUserSettings recordUserPolicyAgreementsIfPossible(final AppUser newUser) {
        DateTime now = DateTime.now();
        return readWriteDAO.create(
                idObjectFactory.newAppUserSettingsBuilder(newUser)
                        .withSetting(AppUserSettings.COOKIES_POLICY_VERSION, cookiesPolicy == null ? 0 : cookiesPolicy.getVersion())
                        .withSetting(AppUserSettings.COOKIES_POLICY_TIMESTAMP, now.getMillis())
                        .withSetting(AppUserSettings.PRIVACY_POLICY_VERSION, privacyPolicy == null ? 0 : privacyPolicy.getVersion())
                        .withSetting(AppUserSettings.PRIVACY_POLICY_TIMESTAMP, now.getMillis())
                        .withSetting(AppUserSettings.TERMS_AND_CONDITIONS_VERSION, termsAndConditions == null ? 0 : termsAndConditions.getVersion())
                        .withSetting(AppUserSettings.TERMS_AND_CONDITIONS_TIMESTAMP, now.getMillis())
                        .build()
        );
    }

    private String securePassword(final String clearCasePassword) {
        if (passwordEncoder != null) {
            return passwordEncoder.encode(clearCasePassword);
        }
        return clearCasePassword;
    }

}
