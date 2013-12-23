package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.ClassList
import groovy.mock.interceptor.MockFor
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 9:18 PM
 */
abstract class AbstractStudentBuilderTest extends AbstractObservableBuilderTest {
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
        testSetField("classList", "classLists", new MockFor(ClassList.class).proxyInstance())
    }
}
