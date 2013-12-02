package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ObservableGImpl
import org.joda.time.LocalDateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:36 PM
 */
class ObservableBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    static class LocalObservable extends ObservableGImpl {

    }

    @BeforeMethod
    def setUp() {
        entity = new LocalObservable()
        builder = new ObservableBuilderGImpl(entity: entity)
    }

    @Test
    void testWithLastObservationTimestamp() {
        testField("lastObservationTimestamp", LocalDateTime.now())
    }
}
