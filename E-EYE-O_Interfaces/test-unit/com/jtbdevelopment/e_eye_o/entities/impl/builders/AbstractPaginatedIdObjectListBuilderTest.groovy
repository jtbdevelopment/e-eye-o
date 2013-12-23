package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.Student
import org.jmock.Mockery
import org.testng.annotations.Test

/**
 * Date: 12/22/13
 * Time: 11:09 AM
 */
abstract class AbstractPaginatedIdObjectListBuilderTest extends AbstractBuilderTest {
    @Test
    void testWithMoreAvailable() {
        testField("moreAvailable", false)
        testField("moreAvailable", true)
    }

    @Test
    void testWithCurrentPage() {
        testField("currentPage", 37)
    }

    @Test
    void testWithPageSize() {
        testField("pageSize", 59)
    }

    @Test
    void testWithEntities() {
        Mockery context = new Mockery();
        testField("entities", [context.mock(AppUser.class), context.mock(ClassList.class), context.mock(Student.class)])
    }
}
