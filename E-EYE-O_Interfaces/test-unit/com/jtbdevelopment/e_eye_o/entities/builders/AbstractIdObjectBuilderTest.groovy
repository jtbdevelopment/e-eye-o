package com.jtbdevelopment.e_eye_o.entities.builders

import org.joda.time.DateTime
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 3:57 PM
 */
abstract class AbstractIdObjectBuilderTest extends AbstractBuilderTest {
    @Test
    public void testWithId() throws Exception {
        testStringField("id")
    }

    @Test
    public void testWithModificationTimestamp() throws Exception {
        testField("modificationTimestamp", DateTime.now())
    }
}
