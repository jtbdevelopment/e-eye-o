package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import org.joda.time.DateTime
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertTrue

/**
 * Date: 11/30/13
 * Time: 4:04 PM
 */
abstract class AbstractTwoPhaseActivityTest extends AbstractAppUserOwnedObjectTest {
    @Test
    public void testDActivityType() {
        testNonStringFieldWithNullValidationError("activityType", null, TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION, TwoPhaseActivity.ACTIVITY_TYPE_CANNOT_BE_NULL)
    }

    @Test
    public void testExpirationTime() throws Exception {
        DateTime before = DateTime.now();
        objectUnderTest = createObjectUnderTest()
        DateTime after = DateTime.now();
        assertTrue(before.compareTo(objectUnderTest.getExpirationTime()) <= 0);
        assertTrue(after.compareTo(objectUnderTest.getExpirationTime()) >= 0);

        testNonStringFieldWithNullValidationError("expirationTime", objectUnderTest.expirationTime, new DateTime(), TwoPhaseActivity.EXPIRATION_TIME_CANNOT_BE_NULL)
    }

    @Test
    public void testSummaryDescription() {
        final String id = "anId";
        final TwoPhaseActivity.Activity activityType = TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION;
        final DateTime now = DateTime.now();
        final boolean archived = true;
        final AppUser user = createAppUser()
        user.firstName = "TEST"
        user.lastName = "NAME"
        objectUnderTest.appUser = user
        objectUnderTest.setExpirationTime(now);
        objectUnderTest.setActivityType(activityType);
        objectUnderTest.setArchived(archived);
        objectUnderTest.setId(id);

        String desc = "Id=" + id + " For=" + user.getSummaryDescription() + " Type=" + activityType.toString() + " Expiration=" + now.toString("YYYY-MM-dd HH:mm:ss") + " Archived=" + archived;

        assertEquals(desc, objectUnderTest.getSummaryDescription());
    }
}
