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
abstract class AbstractUserCreationHelperTest {
    static final String CLEAR_PASSWORD = "CLEAR"
    static final String SECURE_PASSWORD = "SECURE"
    Mockery context
    protected ReadWriteDAO readWriteDAO
    protected IdObjectFactory idObjectFactory
    protected UserCreationHelper userHelper
    protected AppUser userID
    protected AppUser userDAO

    abstract UserCreationHelper createUserHelper();

    @BeforeMethod
    public void setUp() {
        userHelper = createUserHelper()
        context = new Mockery()
        readWriteDAO = context.mock(ReadWriteDAO.class)
        idObjectFactory = context.mock(IdObjectFactory.class)
        userID = context.mock(AppUser.class, "UID")
        userDAO = context.mock(AppUser.class, "UDAO")
        userHelper.readWriteDAO = readWriteDAO
        userHelper.idObjectFactory = idObjectFactory
        userHelper.userNewUserDefaultsCreator = null
        userHelper.cookiesPolicy = null
        userHelper.privacyPolicy = null
        userHelper.termsAndConditions = null
        userHelper.passwordEncoder = null
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
        userHelper.userNewUserDefaultsCreator = helper
        context.checking(new Expectations() {
            {
                oneOf(helper).initializeNewUser(userDAO)
            }
        })
        setupNewUserExpectations(expectedCookieVersion, expectedPrivacyVersion, expectedTermsVersion)
    }

    @Test
    public void testNewUserWithSomeAgreementsRecorded() {
        userHelper.privacyPolicy = context.mock(PrivacyPolicy.class)
        userHelper.termsAndConditions = context.mock(TermsAndConditions.class)

        int expectedCookieVersion = 0
        int expectedPrivacyVersion = 4
        int expectedTermsVersion = 2
        context.checking(new Expectations() {
            {
                oneOf(userHelper.privacyPolicy).getVersion()
                will(returnValue(expectedPrivacyVersion))
                oneOf(userHelper.termsAndConditions).getVersion()
                will(returnValue(expectedTermsVersion))
            }
        })
        setupNewUserExpectations(expectedCookieVersion, expectedPrivacyVersion, expectedTermsVersion)
    }

    @Test
    public void testGenerateActivationRequest() {
        TwoPhaseActivity twoPhaseActivityDAO = setupForActivationRequest()
        assert twoPhaseActivityDAO.is(userHelper.generateActivationRequest(userDAO))
    }

    @Test
    public void testActivateUser() {
        TwoPhaseActivity activity = context.mock(TwoPhaseActivity.class, "TPID")
        TwoPhaseActivity activityUpdate = context.mock(TwoPhaseActivity.class, "TPDAOUP")
        AppUser userID = context.mock(AppUser.class, "ID")
        AppUser userDAO = context.mock(AppUser.class, "DAO");
        AppUser userUpdate = context.mock(AppUser.class, "DAOUP");
        context.checking(new Expectations() {
            {
                oneOf(activity).getAppUser()
                will(returnValue(userID));
                oneOf(userID).getId()
                will(returnValue("2"))
                oneOf(userDAO).getId()
                will(returnValue("2"))
                oneOf(readWriteDAO).get(AppUser.class, "2")
                will(returnValue(userDAO))
                oneOf(readWriteDAO).get(AppUser.class, "2")
                will(returnValue(userUpdate))
                oneOf(userDAO).setActivated(true)
                oneOf(userDAO).setActive(true)
                oneOf(activity).setArchived(true)
                oneOf(activity).setAppUser(userDAO)
                oneOf(readWriteDAO).trustedUpdates([userDAO, activity])
                will(returnValue([userUpdate, activityUpdate]))
            }
        })
        assert userUpdate == userHelper.activateUser(activity)
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
                oneOf(idObjectFactory).newTwoPhaseActivityBuilder(userDAO)
                will(returnValue(activityBuilder as TwoPhaseActivityBuilder))
                oneOf(readWriteDAO).create(twoPhaseActivityID)
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
                oneOf(userID).getPassword()
                will(returnValue(CLEAR_PASSWORD))
                oneOf(userID).setPassword(CLEAR_PASSWORD)
                oneOf(readWriteDAO).create(userID)
                will(returnValue(userDAO))
                oneOf(idObjectFactory).newAppUserSettingsBuilder(userDAO)
                will(returnValue(settingsBuilder as AppUserSettingsBuilder))
                oneOf(readWriteDAO).create(settingsID)
                will(returnValue(settingsDAO))
            }
        })
        TwoPhaseActivity activity = setupForActivationRequest()
        assert activity.is(userHelper.createNewUser(userID))
        assert [AppUserSettings.COOKIES_POLICY_TIMESTAMP, AppUserSettings.COOKIES_POLICY_VERSION, AppUserSettings.TERMS_AND_CONDITIONS_TIMESTAMP, AppUserSettings.TERMS_AND_CONDITIONS_VERSION, AppUserSettings.PRIVACY_POLICY_TIMESTAMP, AppUserSettings.PRIVACY_POLICY_VERSION] as Set ==
                settingsSaved.keySet()
    }

    private void setPasswordEncodingExpectations() {
        PasswordEncoder encoder = context.mock(PasswordEncoder.class)
        userHelper.passwordEncoder = encoder
        context.checking(new Expectations() {
            {
                oneOf(encoder).encode(CLEAR_PASSWORD)
                will(returnValue(SECURE_PASSWORD))
                oneOf(userID).setPassword(SECURE_PASSWORD)
            }
        })
    }
}
