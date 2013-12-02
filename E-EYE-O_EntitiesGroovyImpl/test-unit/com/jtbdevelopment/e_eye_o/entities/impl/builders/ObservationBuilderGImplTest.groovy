package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import com.jtbdevelopment.e_eye_o.entities.impl.ObservationGImpl
import groovy.mock.interceptor.MockFor
import org.joda.time.LocalDateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/2/13
 * Time: 6:38 AM
 */
class ObservationBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new ObservationGImpl()
        builder = new ObservationBuilderGImpl(entity: entity)
    }

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
