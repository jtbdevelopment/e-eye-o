package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.Observable
import org.joda.time.LocalDateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 11/26/13
 * Time: 6:49 AM
 */
class ObservableGImplTest extends AppUserOwnedObjectGImplTest {
    class LocalEntity extends ObservableGImpl {
    }

    @BeforeMethod
    def setUp() {
        objectUnderTest = new LocalEntity()
    }

    @Test
    void testLastObservationTimestamp() {
        testNonStringFieldWithNullValidationError("lastObservationTimestamp", Observable.NEVER_OBSERVED, new LocalDateTime(), Observable.LAST_OBSERVATION_TIME_CANNOT_BE_NULL)
    }
}
