package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.Observable
import org.joda.time.LocalDateTime
import org.testng.annotations.Test

/**
 * Date: 11/26/13
 * Time: 6:49 AM
 */
abstract class AbstractObservableTest extends AbstractAppUserOwnedObjectTest {
    @Test
    void testLastObservationTimestamp() {
        testNonStringFieldWithNullValidationError("lastObservationTimestamp", Observable.NEVER_OBSERVED, new LocalDateTime(), Observable.LAST_OBSERVATION_TIME_CANNOT_BE_NULL)
    }
}
