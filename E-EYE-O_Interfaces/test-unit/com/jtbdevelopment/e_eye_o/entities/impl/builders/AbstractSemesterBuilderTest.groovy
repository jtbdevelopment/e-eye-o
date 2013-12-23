package com.jtbdevelopment.e_eye_o.entities.impl.builders

import org.joda.time.LocalDate
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 9:29 PM
 */
abstract class AbstractSemesterBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithDescription() {
        testStringField("description")
    }

    @Test
    void testWithStart() {
        testField("start", LocalDate.now())
    }

    @Test
    void testWithEnd() {
        testField("end", LocalDate.now())
    }
}
