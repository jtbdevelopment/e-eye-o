package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.SemesterGImpl
import org.joda.time.LocalDate
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 9:29 PM
 */
class SemesterBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new SemesterGImpl()
        builder = new SemesterBuilderGImpl(entity: entity)
    }

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
