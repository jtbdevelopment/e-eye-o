package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ClassListGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:40 PM
 */
class ClassListBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new ClassListGImpl()
        builder = new ClassListBuilderGImpl(entity: entity)
    }

    @Test
    void testWithDescription() {
        testStringField("description")
    }
}
