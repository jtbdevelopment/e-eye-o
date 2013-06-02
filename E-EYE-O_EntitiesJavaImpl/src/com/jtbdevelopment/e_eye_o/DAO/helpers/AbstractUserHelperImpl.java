package com.jtbdevelopment.e_eye_o.DAO.helpers;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

/**
 * Date: 4/6/13
 * Time: 6:26 PM
 * <p/>
 * A base implementation you can extend for new user setup
 * You should implement a createSamplesForNewUser routine
 * to create default entries (if any) for new users
 */
public abstract class AbstractUserHelperImpl implements UserHelper {
    @Autowired
    protected ReadWriteDAO readWriteDAO;

    @Autowired
    protected IdObjectFactory idObjectFactory;

    @Autowired(required = false)
    protected PasswordEncoder passwordEncoder;

    protected abstract void createSamplesForNewUser(final AppUser newUser);

    @Override
    public TwoPhaseActivity setUpNewUser(final AppUser appUser) {
        securePassword(appUser, appUser.getPassword());
        AppUser savedUser = readWriteDAO.create(appUser);
        createSamplesForNewUser(savedUser);
        return generateActivationRequest(savedUser);
    }

    @Override
    public TwoPhaseActivity generateActivationRequest(final AppUser appUser) {
        return readWriteDAO.create(idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION).withExpirationTime(new DateTime().plusDays(1)).build());
    }

    private void securePassword(final AppUser appUser, final String clearCasePassword) {
        if (passwordEncoder != null) {
            appUser.setPassword(passwordEncoder.encode(clearCasePassword));
        }
    }

    @Override
    public void activateUser(final TwoPhaseActivity twoPhaseActivity) {
        twoPhaseActivity.setArchived(true);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        appUser.setActivated(true);
        appUser.setActive(true);
        readWriteDAO.update(Arrays.asList(appUser, twoPhaseActivity));
    }

    @Override
    public TwoPhaseActivity requestResetPassword(AppUser appUser) {
        TwoPhaseActivity requestReset = idObjectFactory.newTwoPhaseActivityBuilder(appUser).withActivityType(TwoPhaseActivity.Activity.PASSWORD_RESET).withExpirationTime(new DateTime().plusDays(1)).build();
        requestReset = readWriteDAO.create(requestReset);
        return requestReset;
    }

    @Override
    public void resetPassword(final TwoPhaseActivity twoPhaseActivity, final String newPassword) {
        twoPhaseActivity.setArchived(true);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        appUser.setActivated(true);
        appUser.setActive(true);
        securePassword(appUser, newPassword);
        readWriteDAO.update(Arrays.asList(appUser, twoPhaseActivity));
    }

}
