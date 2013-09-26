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

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Date: 4/6/13
 * Time: 6:26 PM
 * <p/>
 * A base implementation you can extend for new user setup
 * You should implement a createSamplesForNewUser routine
 * to create default entries (if any) for new users
 */
public abstract class AbstractUserHelperImpl implements UserHelper {
    @Autowired(required = false)
    private TermsAndConditions termsAndConditions;

    @Autowired(required = false)
    private PrivacyPolicy privacyPolicy;

    @Autowired(required = false)
    private CookiesPolicy cookiesPolicy;

    @Autowired
    protected ReadWriteDAO readWriteDAO;

    @Autowired
    protected IdObjectFactory idObjectFactory;

    @Autowired(required = false)
    protected PasswordEncoder passwordEncoder;

    protected abstract void createSamplesForNewUser(final AppUser newUser);

    @Override
    public TwoPhaseActivity setUpNewUser(final AppUser appUser) {
        appUser.setPassword(securePassword(appUser.getPassword()));
        AppUser savedUser = readWriteDAO.create(appUser);
        createDefaultSettingsForNewUser(savedUser);
        createSamplesForNewUser(savedUser);
        return generateActivationRequest(savedUser);
    }

    @Override
    public AppUserSettings createDefaultSettingsForNewUser(final AppUser newUser) {
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

    @Override
    public TwoPhaseActivity generateActivationRequest(final AppUser appUser) {
        return readWriteDAO.create(idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION).withExpirationTime(new DateTime().plusDays(1)).build());
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
        TwoPhaseActivity changeRequest = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.EMAIL_CHANGE).withExpirationTime(new DateTime()).withArchiveFlag(true).build();
        return readWriteDAO.updateUserEmailAddress(changeRequest, newEmailAddress);
    }

    private String securePassword(final String clearCasePassword) {
        if (passwordEncoder != null) {
            return passwordEncoder.encode(clearCasePassword);
        }
        return clearCasePassword;
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

    private boolean hasRecentActivity(final AppUser appUser, final TwoPhaseActivity.Activity activityToCheckFor) {
        //  TODO - make configurable
        final DateTime tooRecent = DateTime.now().minusDays(7);
        Set<TwoPhaseActivity> activities = readWriteDAO.getEntitiesForUser(TwoPhaseActivity.class, appUser);
        return !Collections2.filter(activities, new Predicate<TwoPhaseActivity>() {
            @Override
            public boolean apply(@Nullable final TwoPhaseActivity input) {
                return input != null &&
                        input.getActivityType().equals(activityToCheckFor) &&
                        input.getModificationTimestamp().compareTo(tooRecent) > 0;
            }
        }).isEmpty();
    }

    @Override
    public void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        readWriteDAO.resetUserPassword(twoPhaseActivity, securePassword(newPassword));
    }

}
