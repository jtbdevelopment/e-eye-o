package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;

/**
 * Date: 5/31/13
 * Time: 9:47 PM
 */
public class TwoPhaseActivityBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final TwoPhaseActivity activity = factory.newTwoPhaseActivity(null);
    private final TwoPhaseActivityBuilderImpl builder = new TwoPhaseActivityBuilderImpl(activity);

    @Test
    public void testWithActivityType() throws Exception {
        assertNull(activity.getActivityType());
        for (TwoPhaseActivity.Activity activityType : TwoPhaseActivity.Activity.values()) {
            builder.withActivityType(activityType);
            assertEquals(activityType, activity.getActivityType());
        }
    }

    @Test
    public void testWithExpirationTime() throws Exception {
        DateTime now = DateTime.now();

        builder.withExpirationTime(now);
        assertEquals(now, activity.getExpirationTime());
    }
}
