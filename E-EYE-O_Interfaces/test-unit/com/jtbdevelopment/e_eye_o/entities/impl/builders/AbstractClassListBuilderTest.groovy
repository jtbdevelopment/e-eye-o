package com.jtbdevelopment.e_eye_o.entities.impl.builders

import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:40 PM
 */
abstract class AbstractClassListBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithDescription() {
        testStringField("description")
    }
}
