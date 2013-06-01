package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Date: 5/31/13
 * Time: 10:18 PM
 */
public class TwoPhaseActivityImplTest extends AbstractAppUserOwnedObjectTest<TwoPhaseActivityImpl> {
    public TwoPhaseActivityImplTest() {
        super(TwoPhaseActivityImpl.class);
    }


    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testDefaultActivity() {
        TwoPhaseActivity activity = new TwoPhaseActivityImpl(USER1);
        assertNull(activity.getActivityType());
        validateExpectingError(activity, TwoPhaseActivity.ACTIVITY_TYPE_CANNOT_BE_NULL);
    }

    @Test
    public void testSetActivity() {
        TwoPhaseActivity activity = new TwoPhaseActivityImpl(USER1);
        for (TwoPhaseActivity.Activity activityType : TwoPhaseActivity.Activity.values()) {
            activity.setActivityType(activityType);
            validateNotExpectingError(activity, TwoPhaseActivity.ACTIVITY_TYPE_CANNOT_BE_NULL);
        }
    }

    @Test
    public void testNewActivityDefaultTimestamp() throws Exception {
        DateTime before = new DateTime();
        Thread.sleep(1);
        TwoPhaseActivityImpl activity = new TwoPhaseActivityImpl(USER1);
        Thread.sleep(1);
        DateTime after = new DateTime();
        assertTrue(before.compareTo(activity.getExpirationTime()) < 0);
        assertTrue(after.compareTo(activity.getExpirationTime()) > 0);
    }

    @Test
    public void testValidationOnNullTimestamp() {
        TwoPhaseActivityImpl activity = new TwoPhaseActivityImpl(USER1);
        activity.setExpirationTime(null);
        validateExpectingError(activity, TwoPhaseActivity.EXPIRATION_TIME_CANNOT_BE_NULL);
    }

    @Test
    public void testSetGetTimestamp() {
        DateTime now = new DateTime();
        final TwoPhaseActivity activity = new TwoPhaseActivityImpl(USER1);
        activity.setExpirationTime(now);
        assertEquals(now, activity.getExpirationTime());
        validateNotExpectingError(activity, TwoPhaseActivity.EXPIRATION_TIME_CANNOT_BE_NULL);
    }

    @Test
    public void testSummaryDescription() {
        TwoPhaseActivityImpl activity = new TwoPhaseActivityImpl(USER1);
        final String id = "anId";
        final TwoPhaseActivity.Activity activityType = TwoPhaseActivity.Activity.ACCOUNT_ACTIVATION;
        final DateTime now = DateTime.now();
        final boolean archived = true;
        activity.setExpirationTime(now);
        activity.setActivityType(activityType);
        activity.setArchived(archived);
        activity.setId(id);

        String desc = "Id=" + id + " For=" + USER1.getSummaryDescription() + " Type=" + activityType.toString() + " Expiration="
                + now.toString("YYYY-MM-dd HH:mm:ss") + " Archived=" + archived;

        assertEquals(desc, activity.getSummaryDescription());
    }
}
