package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
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

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Date: 4/6/13
 * Time: 6:26 PM
 * <p/>
 */
@Component
public class UserHelperImpl implements UserHelper {
    @Autowired(required = false)
    private TermsAndConditions termsAndConditions;

    @Autowired(required = false)
    private PrivacyPolicy privacyPolicy;

    @Autowired(required = false)
    private CookiesPolicy cookiesPolicy;

    @Autowired(required = false)
    protected NewUserHelper newUserHelper;

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
        if (newUserHelper != null) {
            newUserHelper.initializeNewUser(savedUser);
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
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, activity));
        return readWriteDAO.get(AppUser.class, appUser.getId());
    }

    @Override
    public boolean canChangeEmailAddress(final AppUser appUser) {
        return !hasRecentActivity(appUser, TwoPhaseActivity.Activity.PASSWORD_RESET);
    }

    @Override
    public boolean canChangePassword(final AppUser appUser) {
        return !hasRecentActivity(appUser, TwoPhaseActivity.Activity.EMAIL_CHANGE);
    }

    @Override
    public TwoPhaseActivity changeEmailAddress(final AppUser appUser, final String newEmailAddress) throws PasswordChangeTooRecent {
        if (!canChangeEmailAddress(appUser)) {
            throw new PasswordChangeTooRecent();
        }
        AppUser loadedUser = readWriteDAO.get(AppUser.class, appUser.getId());
        TwoPhaseActivity changeRequest = idObjectFactory.newTwoPhaseActivityBuilder(loadedUser).withActivityType(TwoPhaseActivity.Activity.EMAIL_CHANGE).withExpirationTime(new DateTime()).build();
        changeRequest = readWriteDAO.create(changeRequest);
        changeRequest.setArchived(true);
        loadedUser.setEmailAddress(newEmailAddress);
        readWriteDAO.trustedUpdates(Arrays.asList(loadedUser, changeRequest));
        return readWriteDAO.get(TwoPhaseActivity.class, changeRequest.getId());
    }

    @Override
    public TwoPhaseActivity requestResetPassword(final AppUser appUser) throws EmailChangeTooRecent {
        if (!canChangePassword(appUser)) {
            throw new EmailChangeTooRecent();
        }
        TwoPhaseActivity requestReset = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.PASSWORD_RESET).withExpirationTime(new DateTime().plusDays(1)).build();
        requestReset = readWriteDAO.create(requestReset);
        return requestReset;
    }

    @Override
    public void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        AppUser appUser = readWriteDAO.get(AppUser.class, twoPhaseActivity.getAppUser().getId());
        appUser.setPassword(newPassword);
        twoPhaseActivity.setArchived(true);
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, twoPhaseActivity));
    }

    @Override
    public void deactivateUser(final AppUser user) {
        AppUser loaded = readWriteDAO.get(AppUser.class, user.getId());
        loaded.setActive(false);
        readWriteDAO.trustedUpdate(loaded);
    }

    @Override
    public AppUserSettings updateSettings(final AppUser appUser, final Map<String, Object> settings) {
        Set<AppUserSettings> entitiesForUser = readWriteDAO.getEntitiesForUser(AppUserSettings.class, appUser, 0, 0);
        if (entitiesForUser.size() != 1) {
            throw new IllegalStateException("Somehow more than 1 app settings");
        }
        AppUserSettings userSettings = entitiesForUser.iterator().next();
        userSettings.updateSettings(settings);
        return readWriteDAO.trustedUpdate(userSettings);
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

    private boolean hasRecentActivity(final AppUser appUser, final TwoPhaseActivity.Activity activityToCheckFor) {
        //  TODO - make configurable
        final DateTime tooRecent = DateTime.now().minusDays(7);
        Set<TwoPhaseActivity> activities = readWriteDAO.getEntitiesForUser(TwoPhaseActivity.class, appUser, 0, 0);
        return !Collections2.filter(activities, new Predicate<TwoPhaseActivity>() {
            @Override
            public boolean apply(@Nullable final TwoPhaseActivity input) {
                return input != null &&
                        input.getActivityType().equals(activityToCheckFor) &&
                        input.getModificationTimestamp().compareTo(tooRecent) > 0;
            }
        }).isEmpty();
    }

    private String securePassword(final String clearCasePassword) {
        if (passwordEncoder != null) {
            return passwordEncoder.encode(clearCasePassword);
        }
        return clearCasePassword;
    }

}
