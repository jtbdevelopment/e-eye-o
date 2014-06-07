package com.jtbdevelopment.e_eye_o.entities.builders

import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:27 PM
 */
abstract class AbstractDeletedObjectBuilderTest extends AbstractAppUserOwnedObjectBuilderTest {
    @Test
    void testWithDeletedId() {
        testStringField("deletedId")
    }
}
