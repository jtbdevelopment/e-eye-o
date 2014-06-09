package com.jtbdevelopment.e_eye_o.entities.builders

import org.joda.time.LocalDateTime
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:36 PM
 */
abstract class AbstractObservableBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithLastObservationTimestamp() {
        testField("lastObservationTimestamp", LocalDateTime.now())
    }
}
