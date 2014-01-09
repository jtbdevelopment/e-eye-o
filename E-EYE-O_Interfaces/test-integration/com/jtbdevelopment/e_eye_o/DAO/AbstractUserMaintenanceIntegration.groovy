package com.jtbdevelopment.e_eye_o.DAO

import com.jtbdevelopment.e_eye_o.DAO.helpers.UserCreationHelper
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserMaintenanceHelper
import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import com.jtbdevelopment.e_eye_o.helpandlegal.CookiesPolicy
import com.jtbdevelopment.e_eye_o.helpandlegal.PrivacyPolicy
import com.jtbdevelopment.e_eye_o.helpandlegal.TermsAndConditions
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.testng.annotations.Test

/**
 * Date: 12/15/13
 * Time: 5:52 PM
 */
abstract class AbstractUserMaintenanceIntegration extends AbstractIntegration {
    //  Save directly, bypassing any DAO code/modification timestamp logic etc
    abstract void saveDirectly(final IdObject entity)

    @Autowired(required = false)
    private TermsAndConditions termsAndConditions;

    @Autowired(required = false)
    private PrivacyPolicy privacyPolicy;

    @Autowired(required = false)
    private CookiesPolicy cookiesPolicy;

    @Autowired
    UserMaintenanceHelper userMaintenanceHelper

    @Autowired
    UserCreationHelper userCreationHelper;

    static Set<AppUser> users = [] as Set;

    @Test(groups = ["integration"],
            dependsOnMethods = [
            "testNewUserCreationPolicies",
            "testGenerateActivationRequest",
            "testActivateUser",
            "testCreatingASecondAppUserSettingsFails",
            "testGettingSettingsCreatesIfNecessary",
            "testPasswordResetRequestFirstTime",
            "testPasswordResetRequestWithIssue",
            "testResetPasswordFirstTime",
            "testChangeEmailFirstTime",
            "testChangeEmailWithException",
            "testCanChangeEmailAddressWithNoRecentPasswordChanges",
            "testCannotChangeEmailAddressWithRecentPasswordChanges",
            "testCanChangePasswordWithNoRecentEmailChanges",
            "testCannotChangePasswordWithRecentEmailChanges",
            "testUpdatingAppUserSettings",
            "testUpdateAppLogout"
            ])
    public void testGetUsersAfter() {

        List<AppUser> actuals = rwDAO.getUsers().findAll { it.emailAddress.endsWith("usermaint.com") }.toList()
        //  Useful to quick debug
        List<String> userEmails = users.collect({ it.emailAddress }).sort()
        List<String> actualEmails = actuals.collect({ it.emailAddress }).sort()
        deepCompare(actuals, users)
    }

    //  No test for calls to new user helper

    @Test(groups = ["integration"])
    public void testNewUserCreationPolicies() {
        DateTime now = DateTime.now()
        AppUser user1 = createUser("CheckPoliciesRecorded")

        users.add(user1)
        AppUserSettings settings = userMaintenanceHelper.getUserSettings(user1)

        assert (cookiesPolicy ? cookiesPolicy.version : 0) == settings.getSettingAsInt(AppUserSettings.COOKIES_POLICY_VERSION, -1)
        assert (termsAndConditions ? termsAndConditions.version : 0) == settings.getSettingAsInt(AppUserSettings.TERMS_AND_CONDITIONS_VERSION, -1)
        assert (privacyPolicy ? privacyPolicy.version : 0) == settings.getSettingAsInt(AppUserSettings.PRIVACY_POLICY_VERSION, -1)
        assert now.compareTo(new DateTime(settings.getSettingAsLong(AppUserSettings.COOKIES_POLICY_TIMESTAMP, 0))) <= 0
        assert now.compareTo(new DateTime(settings.getSettingAsLong(AppUserSettings.TERMS_AND_CONDITIONS_TIMESTAMP, 0))) <= 0
        assert now.compareTo(new DateTime(settings.getSettingAsLong(AppUserSettings.PRIVACY_POLICY_TIMESTAMP, 0))) <= 0
    }

    @Test(groups = ["integration"])
    public void testGenerateActivationRequest() {
        AppUser user = createUser("ActivateReq")
        DateTime now = DateTime.now()
        TwoPhaseActivity activity = userCreationHelper.generateActivationRequest(user)
        users.add(user)
        assert !activity.archived
        assert TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION == activity.activityType
        //  TODO - configurable
        //  With expected expiry +/- 30 seconds
        assert (activity.expirationTime.compareTo(now.plusDays(1).plusSeconds(30)) <= 0)
        assert (activity.expirationTime.compareTo(now.plusDays(1).minusSeconds(30)) >= 0)
    }

    @Test(groups = ["integration"])
    public void testActivateUser() {
        AppUser user1 = createUser("Activate")
        TwoPhaseActivity activity1 = userCreationHelper.generateActivationRequest(user1)

        assert !user1.activated

        user1.active = false
        rwDAO.trustedUpdate(user1)
        AppUser user2 = userCreationHelper.activateUser(activity1)
        users.add(user2)
        TwoPhaseActivity activity2 = rwDAO.get(TwoPhaseActivity, activity1.id)

        assert user2.activated
        assert user2.active
        assert activity2.archived
    }

    @Test(groups = ["integration"], expectedExceptions = [Exception])
    public void testCreatingASecondAppUserSettingsFails() {
        AppUser user1 = createUser("DupeSettingsFails")
        users.add(user1)
        AppUserSettings settingsExist = userMaintenanceHelper.getUserSettings(user1)
        assert settingsExist != null
        rwDAO.create(factory.newAppUserSettings(user1))
        assert false, "Should have failed"
    }

    @Test(groups = ["integration"])
    public void testGettingSettingsCreatesIfNecessary() {
        AppUser user1 = createUser("CreateSettings")
        users.add(user1)
        AppUserSettings settingsExist = userMaintenanceHelper.getUserSettings(user1)
        if (settingsExist) {
            rwDAO.trustedDelete(settingsExist)
        }
        AppUserSettings settingsExist2 = rwDAO.create(factory.newAppUserSettings(user1))
        assert settingsExist.id != settingsExist2.id
        assert userMaintenanceHelper.getUserSettings(user1) == settingsExist2
    }

    @Test(groups = ["integration"])
    public void testPasswordResetRequestFirstTime() {
        AppUser user1 = createUser("ReqResetPassword");
        TwoPhaseActivity reset1 = userMaintenanceHelper.requestResetPassword(user1)
        users.add(user1)
        assert !reset1.archived
        //  7 days +- 30 seconds
        DateTime now = DateTime.now()
        assert (reset1.expirationTime.compareTo(now.plusDays(1).plusSeconds(30)) <= 0)
        assert (reset1.expirationTime.compareTo(now.plusDays(1).minusSeconds(30)) >= 0)
    }

    @Test(groups = ["integration"], expectedExceptions = [UserMaintenanceHelper.EmailChangeTooRecent])
    public void testPasswordResetRequestWithIssue() {
        AppUser user1 = createUser("ReqResetPasswordErr");
        userMaintenanceHelper.changeEmailAddress(user1, "CHANGED@usermaint.com")
        users.add(rwDAO.get(AppUser, user1.id))
        userMaintenanceHelper.requestResetPassword(user1)
    }

    @Test(groups = ["integration"])
    public void testResetPasswordFirstTime() {
        AppUser user1 = createUser("ResetPassword");
        TwoPhaseActivity reset1 = userMaintenanceHelper.requestResetPassword(user1)
        userMaintenanceHelper.resetPassword(reset1, "NEW")
        AppUser user2 = rwDAO.get(AppUser, user1.id)
        TwoPhaseActivity reset2 = rwDAO.get(TwoPhaseActivity, reset1.id)
        users.add(user2)
        //  Can't compare directly in case password encoder is active
        assert user2.password != user1.password
        assert reset2.archived
    }

    @Test(groups = ["integration"])
    public void testChangeEmailFirstTime() {
        String newEmail = "NEWEMAIL@usermaint.com"
        DateTime now = DateTime.now()

        AppUser user1 = createUser("ChangeEMailReq")
        userMaintenanceHelper.changeEmailAddress(user1, newEmail);
        AppUser user2 = rwDAO.get(AppUser, user1.id)
        assert newEmail == user2.emailAddress
        users.add(user2)
        Set<TwoPhaseActivity> activities = rwDAO.getEntitiesForUser(TwoPhaseActivity, user2, 0, 0)
        assert activities.find {
            it.activityType == TwoPhaseActivity.Activity.EMAIL_CHANGE &&
                    it.archived &&
                    it.modificationTimestamp.compareTo(now) >= 0
        }
    }

    @Test(groups = ["integration"], expectedExceptions = [UserMaintenanceHelper.PasswordChangeTooRecent])
    public void testChangeEmailWithException() {
        AppUser user1 = createUser("ChangeEMailFail")
        TwoPhaseActivity activity = userMaintenanceHelper.requestResetPassword(user1)
        userMaintenanceHelper.resetPassword(activity, "newpass")
        users.add(rwDAO.get(AppUser, user1.id))
        userMaintenanceHelper.changeEmailAddress(user1, "WONTWORK@usermaint.com");
    }

    @Test(groups = ["integration"])
    public void testCanChangeEmailAddressWithNoRecentPasswordChanges() {
        AppUser user1 = createUser("CanChangeEmail")
        TwoPhaseActivity activity = userMaintenanceHelper.requestResetPassword(user1)
        userMaintenanceHelper.resetPassword(activity, "newpass")

        //  Hack password reset more than 7 days ago
        //  TODO - make configurable
        activity = rwDAO.get(TwoPhaseActivity, activity.id)
        activity.modificationTimestamp = DateTime.now().minusDays(7).minusMinutes(5)
        saveDirectly(activity)

        userMaintenanceHelper.changeEmailAddress(user1, "CanChangeEmail1@usermaint.com")

        users.add(rwDAO.get(AppUser, user1.id))

        assert userMaintenanceHelper.canChangeEmailAddress(user1)
    }

    @Test(groups = ["integration"])
    public void testCannotChangeEmailAddressWithRecentPasswordChanges() {
        AppUser user1 = createUser("CannotChangeEmail")
        TwoPhaseActivity activity = userMaintenanceHelper.requestResetPassword(user1)
        userMaintenanceHelper.resetPassword(activity, "newpass")

        //  Hack password reset to be almost 7 days ago
        //  TODO - make configurable
        activity = rwDAO.get(TwoPhaseActivity, activity.id)
        activity.modificationTimestamp = DateTime.now().minusDays(6).minusHours(23).minusMinutes(55)
        saveDirectly(activity)

        users.add(rwDAO.get(AppUser, user1.id))

        assert !userMaintenanceHelper.canChangeEmailAddress(user1)
    }

    @Test(groups = ["integration"])
    public void testCanChangePasswordWithNoRecentEmailChanges() {
        AppUser user1 = createUser("CanChangePassword")
        userMaintenanceHelper.changeEmailAddress(user1, "CanChangePassword1@usermaint.com")

        TwoPhaseActivity activity = rwDAO.getEntitiesForUser(TwoPhaseActivity, user1, 0, 0).find {
            it.activityType == TwoPhaseActivity.Activity.EMAIL_CHANGE
        }

        //  Hack email reset just more than 7 days ago
        //  TODO - make configurable
        activity = rwDAO.get(TwoPhaseActivity, activity.id)
        activity.modificationTimestamp = DateTime.now().minusDays(7).minusMinutes(5)
        saveDirectly(activity)

        users.add(rwDAO.get(AppUser, user1.id))

        assert userMaintenanceHelper.canChangePassword(user1)
    }

    @Test(groups = ["integration"])
    public void testCannotChangePasswordWithRecentEmailChanges() {
        AppUser user1 = createUser("CannotChangePassword")
        userMaintenanceHelper.changeEmailAddress(user1, "CannotChangePassword1@usermaint.com")

        TwoPhaseActivity activity = rwDAO.getEntitiesForUser(TwoPhaseActivity, user1, 0, 0).find {
            it.activityType == TwoPhaseActivity.Activity.EMAIL_CHANGE
        }

        //  Hack email reset just less than 7 days ago
        //  TODO - make configurable
        activity = rwDAO.get(TwoPhaseActivity, activity.id)
        activity.modificationTimestamp = DateTime.now().minusDays(6).minusHours(23).minusMinutes(55)
        saveDirectly(activity)

        users.add(rwDAO.get(AppUser, user1.id))

        assert !userMaintenanceHelper.canChangePassword(user1)
    }

    @Test(groups = ["integration"])
    public void testUpdatingAppUserSettings() {
        AppUser user = createUser("UpdateSettings")
        users.add(user)
        Map<String, Object> settings = ["x": 1, "s": "s"]
        userMaintenanceHelper.updateSettings(user, settings)
        AppUserSettings updatedSettings = userMaintenanceHelper.getUserSettings(user)
        settings.each {
            String key, Object value ->
                assert value.toString() == updatedSettings.getSettingAsString(key, "")
        }
    }

    @Test(groups = ["integration"])
    public void testUpdateAppLogout() {
        AppUser user1 = createUser("UpdateLogout")
        DateTime now = DateTime.now()
        Thread.sleep(1000)
        rwDAO.updateAppUserLogout(user1)
        AppUser user2 = rwDAO.get(AppUser, user1.id)
        users.add(user2)
        assert now.compareTo(user2.lastLogout) <= 0
    }

    @Override
    protected AppUser createUser(String baseName) {
        String email = baseName + "@usermaint.com"
        userCreationHelper.createNewUser(factory.newAppUserBuilder().withFirstName(baseName).withEmailAddress(email).withPassword("X").build())
        rwDAO.getUser(email)
    }
}
