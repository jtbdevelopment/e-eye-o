package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import com.jtbdevelopment.e_eye_o.entities.impl.TwoPhaseActivityGImpl
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:49 PM
 */
class TwoPhaseActivityBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new TwoPhaseActivityGImpl()
        builder = new TwoPhaseActivityBuilderGImpl(entity: entity)
    }

    @Test
    void testWithActivityType() {
        testField("activityType", TwoPhaseActivity.Activity.EMAIL_CHANGE)
    }

    @Test
    void testWithExpirationTime() {
        testField("expirationTime", DateTime.now())
    }
}
