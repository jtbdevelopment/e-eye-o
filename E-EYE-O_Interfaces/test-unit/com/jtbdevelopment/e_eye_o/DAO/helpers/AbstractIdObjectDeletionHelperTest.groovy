package com.jtbdevelopment.e_eye_o.DAO.helpers

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserSettingsBuilder
import com.jtbdevelopment.e_eye_o.entities.builders.TwoPhaseActivityBuilder
import com.jtbdevelopment.e_eye_o.helpandlegal.PrivacyPolicy
import com.jtbdevelopment.e_eye_o.helpandlegal.TermsAndConditions
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
abstract class AbstractIdObjectDeletionHelperTest {
    static final String CLEAR_PASSWORD = "CLEAR"
    static final String SECURE_PASSWORD = "SECURE"
    static final String NEW_EMAIL = "new@new.com"
    Mockery context
    protected ReadWriteDAO readWriteDAO
    protected IdObjectFactory idObjectFactory
    protected IdObjectDeletionHelper deletionHelper
    protected AppUser userID
    protected AppUser userDAO

    abstract IdObjectDeletionHelper createIdObjectDeletionHelper();

    @BeforeMethod
    public void setUp() {
        deletionHelper = createIdObjectDeletionHelper()
        context = new Mockery()
        readWriteDAO = context.mock(ReadWriteDAO.class)
        idObjectFactory = context.mock(IdObjectFactory.class)
        userID = context.mock(AppUser.class, "UID")
        userDAO = context.mock(AppUser.class, "UDAO")
        deletionHelper.readWriteDAO = readWriteDAO
        deletionHelper.idObjectFactory = idObjectFactory
        deletionHelper.newUserHelper = null
        deletionHelper.cookiesPolicy = null
        deletionHelper.privacyPolicy = null
        deletionHelper.termsAndConditions = null
        deletionHelper.passwordEncoder = null
    }

    @Test
    public void testNewUserWithAllOptionalHelpersNull() {
        int expectedCookieVersion = 0
        int expectedPrivacyVersion = 0
        int expectedTermsVersion = 0
        setupNewUserExpectations(expectedCookieVersion, expectedPrivacyVersion, expectedTermsVersion)
    }

    @Test
    public void testNewUserWithPasswordEncoder() {
        int expectedCookieVersion = 0
        int expectedPrivacyVersion = 0
        int expectedTermsVersion = 0
        setPasswordEncodingExpectations()
        setupNewUserExpectations(expectedCookieVersion, expectedPrivacyVersion, expectedTermsVersion)
    }

    @Test
    public void testNewUserWithNewUserHelper() {
        int expectedCookieVersion = 0
        int expectedPrivacyVersion = 0
        int expectedTermsVersion = 0
        UserNewUserDefaultsCreator helper = context.mock(UserNewUserDefaultsCreator.class)
        deletionHelper.newUserHelper = helper
        context.checking(new Expectations() {
            {
                one(helper).initializeNewUser(userDAO)
            }
        })
        setupNewUserExpectations(expectedCookieVersion, expectedPrivacyVersion, expectedTermsVersion)
    }

    @Test
    public void testNewUserWithSomeAgreementsRecorded() {
        deletionHelper.privacyPolicy = context.mock(PrivacyPolicy.class)
        deletionHelper.termsAndConditions = context.mock(TermsAndConditions.class)

        int expectedCookieVersion = 0
        int expectedPrivacyVersion = 4
        int expectedTermsVersion = 2
        context.checking(new Expectations() {
            {
                one(deletionHelper.privacyPolicy).getVersion()
                will(returnValue(expectedPrivacyVersion))
                one(deletionHelper.termsAndConditions).getVersion()
                will(returnValue(expectedTermsVersion))
            }
        })
        setupNewUserExpectations(expectedCookieVersion, expectedPrivacyVersion, expectedTermsVersion)
    }

    @Test
    public void testGenerateActivationRequest() {
        TwoPhaseActivity twoPhaseActivityDAO = setupForActivationRequest()
        assert twoPhaseActivityDAO.is(deletionHelper.generateActivationRequest(userDAO))
    }

    @Test
    public void testResetPasswordNoEncoder() {
        TwoPhaseActivity activity = context.mock(TwoPhaseActivity.class)
        context.checking(new Expectations() {
            {
                one(readWriteDAO).resetUserPassword(activity, CLEAR_PASSWORD)
                will(returnValue(activity))
            }
        })
        deletionHelper.resetPassword(activity, CLEAR_PASSWORD)
    }

    @Test
    public void testResetPasswordWithEncoder() {
        TwoPhaseActivity activity = context.mock(TwoPhaseActivity.class)
        context.checking(new Expectations() {
            {
                one(readWriteDAO).resetUserPassword(activity, SECURE_PASSWORD)
                will(returnValue(activity))
            }
        })
        setPasswordEncodingExpectations()
        deletionHelper.resetPassword(activity, CLEAR_PASSWORD)
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
                one(readWriteDAO).getEntitiesForUser(TwoPhaseActivity.class, userDAO, 0, 0)
                will(returnValue([] as Set))
                one(idObjectFactory).newTwoPhaseActivityBuilder(userDAO)
                will(returnValue(activityBuilder as TwoPhaseActivityBuilder))
                one(readWriteDAO).updateUserEmailAddress(twoPhaseActivityID, NEW_EMAIL)
                will(returnValue(twoPhaseActivityDAO))
            }
        })
        deletionHelper.changeEmailAddress(userDAO, NEW_EMAIL)
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
        assert twoPhaseActivityDAO.is(deletionHelper.requestResetPassword(userDAO))
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

        assert deletionHelper.canChangeEmailAddress(userID)
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

        assert !deletionHelper.canChangeEmailAddress(userID)
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

        assert deletionHelper.canChangePassword(userID)
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

        assert !deletionHelper.canChangePassword(userID)
    }

    private TwoPhaseActivity createActivity(final TwoPhaseActivity.Activity activity, final DateTime modificationTime, final DateTime expiry = null) {
        [
                getModificationTimestamp: { return modificationTime },
                getActivityType: { return activity },
                getExpirationTime: { return expiry }
        ] as TwoPhaseActivity
    }

    private TwoPhaseActivity setupForActivationRequest() {
        TwoPhaseActivity twoPhaseActivityID = context.mock(TwoPhaseActivity.class, "TPAID")
        TwoPhaseActivity twoPhaseActivityDAO = context.mock(TwoPhaseActivity.class, "TPADAO")
        Map activityBuilder = [:]
        DateTime before = DateTime.now().plusDays(1)
        activityBuilder += [withExpirationTime: {
            DateTime dt ->
                assert before.compareTo(dt) <= 0 && dt.compareTo(DateTime.now().plusDays(1)) <= 0
                return activityBuilder as TwoPhaseActivityBuilder;
        }]
        activityBuilder += [withActivityType: {
            TwoPhaseActivity.Activity a ->
                assert TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION == a
                return activityBuilder as TwoPhaseActivityBuilder
        }]
        activityBuilder += [build: { return twoPhaseActivityID }]
        context.checking(new Expectations() {
            {
                one(idObjectFactory).newTwoPhaseActivityBuilder(userDAO)
                will(returnValue(activityBuilder as TwoPhaseActivityBuilder))
                one(readWriteDAO).create(twoPhaseActivityID)
                will(returnValue(twoPhaseActivityDAO))
            }
        })
        twoPhaseActivityDAO
    }

    private void setupNewUserExpectations(int expectedCookieVersion, int expectedPrivacyVersion, int expectedTermsVersion) {
        AppUserSettings settingsID = context.mock(AppUserSettings.class, "AUSID")
        AppUserSettings settingsDAO = context.mock(AppUserSettings.class, "AUSDAO")
        Map settingsBuilder = [:]
        DateTime before = DateTime.now()
        Map settingsSaved = [:]
        settingsBuilder += [withSetting: {
            String name, Object o ->
                settingsSaved += [(name): o.toString()]
                switch (name) {
                    case AppUserSettings.COOKIES_POLICY_VERSION:
                        assert expectedCookieVersion == o
                        break;
                    case AppUserSettings.PRIVACY_POLICY_VERSION:
                        assert expectedPrivacyVersion == o
                        break
                    case AppUserSettings.TERMS_AND_CONDITIONS_VERSION:
                        assert expectedTermsVersion == o
                        break;
                    case AppUserSettings.COOKIES_POLICY_TIMESTAMP:
                    case AppUserSettings.TERMS_AND_CONDITIONS_TIMESTAMP:
                    case AppUserSettings.PRIVACY_POLICY_TIMESTAMP:
                        DateTime recorded = new DateTime(o)
                        assert before.compareTo(recorded) <= 0 && DateTime.now().compareTo(recorded) >= 0
                        break;
                }
                return settingsBuilder as AppUserSettingsBuilder
        }]
        settingsBuilder += [build: { return settingsID }]

        context.checking(new Expectations() {
            {
                one(userID).getPassword()
                will(returnValue(CLEAR_PASSWORD))
                one(userID).setPassword(CLEAR_PASSWORD)
                one(readWriteDAO).create(userID)
                will(returnValue(userDAO))
                one(idObjectFactory).newAppUserSettingsBuilder(userDAO)
                will(returnValue(settingsBuilder as AppUserSettingsBuilder))
                one(readWriteDAO).create(settingsID)
                will(returnValue(settingsDAO))
            }
        })
        TwoPhaseActivity activity = setupForActivationRequest()
        assert activity.is(deletionHelper.createNewUser(userID))
        assert [AppUserSettings.COOKIES_POLICY_TIMESTAMP, AppUserSettings.COOKIES_POLICY_VERSION, AppUserSettings.TERMS_AND_CONDITIONS_TIMESTAMP, AppUserSettings.TERMS_AND_CONDITIONS_VERSION, AppUserSettings.PRIVACY_POLICY_TIMESTAMP, AppUserSettings.PRIVACY_POLICY_VERSION] as Set ==
                settingsSaved.keySet()
    }

    private void setPasswordEncodingExpectations() {
        PasswordEncoder encoder = context.mock(PasswordEncoder.class)
        deletionHelper.passwordEncoder = encoder
        context.checking(new Expectations() {
            {
                one(encoder).encode(CLEAR_PASSWORD)
                will(returnValue(SECURE_PASSWORD))
                one(userID).setPassword(SECURE_PASSWORD)
            }
        })
    }
}
