package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ClassListGImpl
import com.jtbdevelopment.e_eye_o.entities.impl.StudentGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 9:18 PM
 */
class StudentBuilderGImplTest extends ObservableBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new StudentGImpl()
        builder = new StudentBuilderGImpl(entity: entity)
    }

    @Test
    void testWithFirstName() {
        testStringField("firstName")
    }

    @Test
    void testWithLastName() {
        testStringField("lastName")
    }

    @Test
    void testClassList() {
        testSetField("classList", "classLists", new ClassListGImpl(id: "X"))
    }
}
