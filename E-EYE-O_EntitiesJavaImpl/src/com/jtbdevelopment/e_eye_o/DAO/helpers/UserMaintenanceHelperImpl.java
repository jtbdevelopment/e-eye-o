package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

@Component
public class UserMaintenanceHelperImpl implements UserMaintenanceHelper {
    @Autowired(required = false)
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected ReadWriteDAO readWriteDAO;
    @Autowired
    protected IdObjectFactory idObjectFactory;

    @Override
    public AppUserSettings getUserSettings(final AppUser appUser) {
        Set<AppUserSettings> entitiesForUser = readWriteDAO.getEntitiesForUser(AppUserSettings.class, appUser, 0, 0);
        if (entitiesForUser.isEmpty()) {
            return readWriteDAO.create(idObjectFactory.newAppUserSettings(readWriteDAO.get(AppUser.class, appUser.getId())));
        }

        return entitiesForUser.iterator().next();
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
    public TwoPhaseActivity changeEmailAddress(final AppUser appUser, final String newEmailAddress) throws UserMaintenanceHelper.PasswordChangeTooRecent {
        if (!canChangeEmailAddress(appUser)) {
            throw new UserMaintenanceHelper.PasswordChangeTooRecent();
        }
        AppUser loadedUser = readWriteDAO.get(AppUser.class, appUser.getId());
        TwoPhaseActivity changeRequest = idObjectFactory.newTwoPhaseActivityBuilder(loadedUser).withActivityType(TwoPhaseActivity.Activity.EMAIL_CHANGE).withExpirationTime(DateTime.now()).build();
        changeRequest = readWriteDAO.create(changeRequest);
        changeRequest.setArchived(true);
        loadedUser.setEmailAddress(newEmailAddress);
        readWriteDAO.trustedUpdates(Arrays.asList(loadedUser, changeRequest));
        return readWriteDAO.get(TwoPhaseActivity.class, changeRequest.getId());
    }

    @Override
    public TwoPhaseActivity requestResetPassword(final AppUser appUser) throws UserMaintenanceHelper.EmailChangeTooRecent {
        if (!canChangePassword(appUser)) {
            throw new UserMaintenanceHelper.EmailChangeTooRecent();
        }
        //  TODO - make 1 day configurable
        TwoPhaseActivity requestReset = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.PASSWORD_RESET).withExpirationTime(DateTime.now().plusDays(1)).build();
        requestReset = readWriteDAO.create(requestReset);
        return requestReset;
    }

    @Override
    public void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        AppUser appUser = readWriteDAO.get(AppUser.class, twoPhaseActivity.getAppUser().getId());
        appUser.setPassword(securePassword(newPassword));
        twoPhaseActivity.setArchived(true);
        twoPhaseActivity.setAppUser(appUser);
        readWriteDAO.trustedUpdates(Arrays.asList(appUser, twoPhaseActivity));
    }

    @Override
    public AppUserSettings updateSettings(final AppUser appUser, final Map<String, Object> settings) {
        AppUserSettings userSettings = getUserSettings(appUser);
        userSettings.updateSettings(settings);
        return readWriteDAO.trustedUpdate(userSettings);
    }

    boolean hasRecentActivity(final AppUser appUser, final TwoPhaseActivity.Activity activityToCheckFor) {
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

    String securePassword(final String clearCasePassword) {
        if (passwordEncoder != null) {
            return passwordEncoder.encode(clearCasePassword);
        }
        return clearCasePassword;
    }
}