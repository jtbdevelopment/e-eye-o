package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import groovy.mock.interceptor.MockFor
import org.joda.time.LocalDateTime
import org.testng.annotations.Test

/**
 * Date: 12/2/13
 * Time: 6:38 AM
 */
abstract class AbstractObservationBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithObservationSubject() {
        testField("observationSubject", new MockFor(ClassList.class).proxyInstance())
    }

    @Test
    void testWithObservationTimestamp() {
        testField("observationTimestamp", LocalDateTime.now())
    }

    @Test
    void testWithSignificant() {
        testField("significant", true)
    }

    @Test
    void testWithCategories() {
        testSetField("category", "categories", new MockFor(ObservationCategory.class).proxyInstance())
    }

    @Test
    void testWithComment() {
        testStringField("comment")
    }
}
