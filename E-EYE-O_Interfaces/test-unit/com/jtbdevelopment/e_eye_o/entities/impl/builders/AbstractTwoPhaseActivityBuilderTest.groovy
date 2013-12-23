package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import org.joda.time.DateTime
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:49 PM
 */
abstract class AbstractTwoPhaseActivityBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithActivityType() {
        testField("activityType", TwoPhaseActivity.Activity.EMAIL_CHANGE)
    }

    @Test
    void testWithExpirationTime() {
        testField("expirationTime", DateTime.now())
    }
}
