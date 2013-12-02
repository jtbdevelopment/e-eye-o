package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ObservationCategoryGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:03 PM
 */
class ObservationCategoryBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new ObservationCategoryGImpl()
        builder = new ObservationCategoryBuilderGImpl(entity: entity)
    }

    @Test
    void testWithShortName() {
        testStringField("shortName")
    }

    @Test
    void testWithDescription() {
        testStringField("description")
    }
}
