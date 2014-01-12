package com.jtbdevelopment.e_eye_o.entities.builders

import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:03 PM
 */
abstract class AbstractObservationCategoryBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithShortName() {
        testStringField("shortName")
    }

    @Test
    void testWithDescription() {
        testStringField("description")
    }
}
