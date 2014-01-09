package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import com.jtbdevelopment.e_eye_o.entities.builders.TwoPhaseActivityBuilder
import org.jmock.Expectations
import org.jmock.Mockery
import org.joda.time.DateTime
import org.springframework.security.crypto.password.PasswordEncoder
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/15/13
 * Time: 5:52 PM
 */
abstract class AbstractUserMaintenanceHelperTest {
    static final String CLEAR_PASSWORD = "CLEAR"
    static final String SECURE_PASSWORD = "SECURE"
    static final String NEW_EMAIL = "new@new.com"
    Mockery context
    protected ReadWriteDAO readWriteDAO
    protected IdObjectFactory idObjectFactory
    protected UserMaintenanceHelper userHelper
    protected AppUser userID
    protected AppUser userDAO

    abstract UserMaintenanceHelper createUserMaintenanceHelper();

    @BeforeMethod
    public void setUp() {
        userHelper = createUserMaintenanceHelper()
        context = new Mockery()
        readWriteDAO = context.mock(ReadWriteDAO.class)
        idObjectFactory = context.mock(IdObjectFactory.class)
        userID = context.mock(AppUser.class, "UID")
        userDAO = context.mock(AppUser.class, "UDAO")
        userHelper.readWriteDAO = readWriteDAO
        userHelper.idObjectFactory = idObjectFactory
        userHelper.passwordEncoder = null
    }

    @Test
    public void testResetPasswordNoEncoder() {
        TwoPhaseActivity activity = context.mock(TwoPhaseActivity.class)
        context.checking(new Expectations() {
            {
                one(activity).getAppUser();
                will(returnValue(userID));
                one(userID).getId()
                will(returnValue("ID"));
                one(readWriteDAO).get(AppUser.class, "ID");
                will(returnValue(userDAO))
                one(userDAO).setPassword(CLEAR_PASSWORD);
                one(activity).setArchived(true);
                one(readWriteDAO).trustedUpdates(Arrays.asList(userDAO, activity));
                will(returnValue(Arrays.asList(activity, userDAO)));
            }
        })
        userHelper.resetPassword(activity, CLEAR_PASSWORD)
    }

    @Test
    public void testResetPasswordWithEncoder() {
        TwoPhaseActivity activity = context.mock(TwoPhaseActivity.class)
        context.checking(new Expectations() {
            {
                one(activity).getAppUser();
                will(returnValue(userID));
                one(userID).getId()
                will(returnValue("ID"));
                one(readWriteDAO).get(AppUser.class, "ID");
                will(returnValue(userDAO))
                one(userDAO).setPassword(SECURE_PASSWORD);
                one(activity).setArchived(true);
                one(readWriteDAO).trustedUpdates(Arrays.asList(userDAO, activity));
                will(returnValue(Arrays.asList(activity, userDAO)));
            }
        })
        setPasswordEncodingExpectations()
        userHelper.resetPassword(activity, CLEAR_PASSWORD)
    }

    @Test
    public void testChangeEmailRequestWithNoIssues() {
        TwoPhaseActivity twoPhaseActivityID = context.mock(TwoPhaseActivity.class, "TPAID")
        TwoPhaseActivity twoPhaseActivityDAO = context.mock(TwoPhaseActivity.class, "TPADAO")
        Map activityBuilder = [:]
        DateTime before = DateTime.now()
        activityBuilder += [withExpirationTime: {
            DateTime dt ->
                assert before.compareTo(dt) <= 0 && dt.compareTo(DateTime.now()) <= 0
                return activityBuilder as TwoPhaseActivityBuilder;
        }]
        activityBuilder += [withActivityType: {
            TwoPhaseActivity.Activity a ->
                assert TwoPhaseActivity.Activity.EMAIL_CHANGE == a
                return activityBuilder as TwoPhaseActivityBuilder
        }]
        activityBuilder += [withArchived: {
            boolean b ->
                assert b;
                return activityBuilder as TwoPhaseActivityBuilder
        }]
        activityBuilder += [build: { return twoPhaseActivityID }]
        context.checking(new Expectations() {
            {
                one(userID).getId();
                will(returnValue("ID"))
                one(readWriteDAO).get(AppUser.class, "ID");
                will(returnValue(userDAO));
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userID, 0, 0)
                will(returnValue([] as Set))
                one(idObjectFactory).newTwoPhaseActivityBuilder(userDAO)
                will(returnValue(activityBuilder as TwoPhaseActivityBuilder))
                one(readWriteDAO).create(twoPhaseActivityID)
                will(returnValue(twoPhaseActivityDAO))
                one(twoPhaseActivityDAO).setArchived(true);
                one(userDAO).setEmailAddress(NEW_EMAIL);
                one(readWriteDAO).trustedUpdates(Arrays.asList(userDAO, twoPhaseActivityDAO))
                will(returnValue(Arrays.asList(twoPhaseActivityDAO, userDAO)))
                one(twoPhaseActivityDAO).getId()
                will(returnValue("TID"))
                one(readWriteDAO).get(TwoPhaseActivity.class, "TID");
                will(returnValue(twoPhaseActivityDAO))
            }
        })
        userHelper.changeEmailAddress(userID, NEW_EMAIL)
    }

    @Test(expectedExceptions = [UserMaintenanceHelper.PasswordChangeTooRecent])
    public void testChangeEmailRequestWithException() {
        TwoPhaseActivity twoPhaseActivityDAO = context.mock(TwoPhaseActivity.class, "TPADAO")
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userDAO, 0, 0)
                will(returnValue([twoPhaseActivityDAO] as Set))
                one(twoPhaseActivityDAO).getActivityType()
                will(returnValue(TwoPhaseActivity.Activity.PASSWORD_RESET))
                one(twoPhaseActivityDAO).getModificationTimestamp()
                will(returnValue(DateTime.now().minusDays(1)))
            }
        })
        userHelper.changeEmailAddress(userDAO, NEW_EMAIL)
    }

    @Test
    public void testPasswordResetRequestNoIssues() {
        TwoPhaseActivity twoPhaseActivityID = context.mock(TwoPhaseActivity.class, "TPAID")
        TwoPhaseActivity twoPhaseActivityDAO = context.mock(TwoPhaseActivity.class, "TPADAO")
        Map activityBuilder = [:]
        DateTime before = DateTime.now()
        activityBuilder += [withExpirationTime: {
            DateTime dt ->
                assert before.compareTo(dt) < 0 && dt.compareTo(DateTime.now().plusDays(1)) <= 0
                return activityBuilder as TwoPhaseActivityBuilder;
        }]
        activityBuilder += [withActivityType: {
            TwoPhaseActivity.Activity a ->
                assert TwoPhaseActivity.Activity.PASSWORD_RESET == a
                return activityBuilder as TwoPhaseActivityBuilder
        }]
        activityBuilder += [build: { return twoPhaseActivityID }]
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userDAO, 0, 0)
                will(returnValue([] as Set))
                one(idObjectFactory).newTwoPhaseActivityBuilder(userDAO)
                will(returnValue(activityBuilder as TwoPhaseActivityBuilder))
                one(readWriteDAO).create(twoPhaseActivityID)
                will(returnValue(twoPhaseActivityDAO))
            }
        })
        assert twoPhaseActivityDAO.is(userHelper.requestResetPassword(userDAO))
    }

    @Test(expectedExceptions = [UserMaintenanceHelper.EmailChangeTooRecent])
    public void testPasswordResetRequestWithIssue() {
        TwoPhaseActivity twoPhaseActivityDAO = context.mock(TwoPhaseActivity.class, "TPADAO")
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userDAO, 0, 0)
                will(returnValue([twoPhaseActivityDAO] as Set))
                one(twoPhaseActivityDAO).getActivityType()
                will(returnValue(TwoPhaseActivity.Activity.EMAIL_CHANGE))
                one(twoPhaseActivityDAO).getModificationTimestamp()
                will(returnValue(DateTime.now().minusDays(1)))
            }
        })
        userHelper.requestResetPassword(userDAO)
    }

    @Test
    public void testCanChangeEmailAddressWithNoRecentPasswordChanges() {
        TwoPhaseActivity recentEmailChange = createActivity(TwoPhaseActivity.Activity.EMAIL_CHANGE, DateTime.now())
        TwoPhaseActivity oldPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now().minusDays(10))
        TwoPhaseActivity olderPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now().minusDays(15))
        TwoPhaseActivity accountActivation = createActivity(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION, DateTime.now().minusDays(20))
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userID, 0, 0)
                will(returnValue([recentEmailChange, olderPasswordChange, oldPasswordChange, accountActivation] as Set))
            }
        })

        assert userHelper.canChangeEmailAddress(userID)
    }

    @Test
    public void testCannotChangeEmailAddressWithRecentPasswordChanges() {
        TwoPhaseActivity recentEmailChange = createActivity(TwoPhaseActivity.Activity.EMAIL_CHANGE, DateTime.now())
        TwoPhaseActivity oldPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now().minusDays(5))
        TwoPhaseActivity olderPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now().minusDays(15))
        TwoPhaseActivity accountActivation = createActivity(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION, DateTime.now().minusDays(20))
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userID, 0, 0)
                will(returnValue([recentEmailChange, olderPasswordChange, oldPasswordChange, accountActivation] as Set))
            }
        })

        assert !userHelper.canChangeEmailAddress(userID)
    }

    @Test
    public void testCanChangePasswordWithNoRecentEmailChanges() {
        TwoPhaseActivity recentEmailChange = createActivity(TwoPhaseActivity.Activity.EMAIL_CHANGE, DateTime.now().minusDays(8))
        TwoPhaseActivity oldPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now())
        TwoPhaseActivity olderPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now().minusDays(15))
        TwoPhaseActivity accountActivation = createActivity(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION, DateTime.now().minusDays(20))
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userID, 0, 0)
                will(returnValue([recentEmailChange, olderPasswordChange, oldPasswordChange, accountActivation] as Set))
            }
        })

        assert userHelper.canChangePassword(userID)
    }

    @Test
    public void testCannotChangePasswordWithRecentEmailChanges() {
        TwoPhaseActivity recentEmailChange = createActivity(TwoPhaseActivity.Activity.EMAIL_CHANGE, DateTime.now().minusDays(5))
        TwoPhaseActivity oldPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now())
        TwoPhaseActivity olderPasswordChange = createActivity(TwoPhaseActivity.Activity.PASSWORD_RESET, DateTime.now().minusDays(15))
        TwoPhaseActivity accountActivation = createActivity(TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION, DateTime.now().minusDays(20))
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userID, 0, 0)
                will(returnValue([recentEmailChange, olderPasswordChange, oldPasswordChange, accountActivation] as Set))
            }
        })

        assert !userHelper.canChangePassword(userID)
    }

    @Test
    public void testUpdatingAppUserSettings() {
        Map<String, Object> settings = ["x": 1, "s": "s"]
        AppUserSettings settingsDAO = context.mock(AppUserSettings.class, "DAO");
        AppUserSettings settingsUpDAO = context.mock(AppUserSettings.class, "DAOUP");
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(AppUserSettings.class, userID, 0, 0)
                will(returnValue([settingsDAO] as Set))
                one(settingsDAO).updateSettings(settings)
                one(readWriteDAO).trustedUpdate(settingsDAO)
                will(returnValue(settingsUpDAO))
            }
        })
        assert settingsUpDAO == userHelper.updateSettings(userID, settings)
    }

    @Test
    public void testGetUserSettingsWhereExist() {
        AppUserSettings settingsDAO = context.mock(AppUserSettings.class, "DAO");
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(AppUserSettings.class, userID, 0, 0)
                will(returnValue([settingsDAO] as Set))
            }
        })
        assert settingsDAO == userHelper.getUserSettings(userID)
    }

    @Test
    public void testGetUserSettingsCreatedIfNotPreset() {
        AppUserSettings settingsID = context.mock(AppUserSettings.class, "ID");
        AppUserSettings settingsDAO = context.mock(AppUserSettings.class, "DAO");
        context.checking(new Expectations() {
            {
                one(readWriteDAO).getEntitiesForUser(AppUserSettings.class, userID, 0, 0)
                will(returnValue([] as Set))
                one(userID).getId()
                will(returnValue("ID"));
                one(readWriteDAO).get(AppUser, "ID")
                will(returnValue(userDAO))
                one(idObjectFactory).newAppUserSettings(userDAO)
                will(returnValue(settingsID))
                one(readWriteDAO).create(settingsID)
                will(returnValue(settingsDAO))
            }
        })
        assert settingsDAO == userHelper.getUserSettings(userID)
    }

    private static TwoPhaseActivity createActivity(
            final TwoPhaseActivity.Activity activity, final DateTime modificationTime, final DateTime expiry = null) {
        [
                getModificationTimestamp: { return modificationTime },
                getActivityType: { return activity },
                getExpirationTime: { return expiry }
        ] as TwoPhaseActivity
    }

    private void setPasswordEncodingExpectations() {
        PasswordEncoder encoder = context.mock(PasswordEncoder.class)
        userHelper.passwordEncoder = encoder
        context.checking(new Expectations() {
            {
                one(encoder).encode(CLEAR_PASSWORD)
                will(returnValue(SECURE_PASSWORD))
                one(userID).setPassword(SECURE_PASSWORD)
            }
        })
    }
}
