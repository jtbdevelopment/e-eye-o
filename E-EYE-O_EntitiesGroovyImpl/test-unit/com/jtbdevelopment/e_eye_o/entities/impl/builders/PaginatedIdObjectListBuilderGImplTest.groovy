package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.Student
import com.jtbdevelopment.e_eye_o.entities.impl.PaginatedIdObjectListGImpl
import org.jmock.Mockery
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/22/13
 * Time: 11:09 AM
 */
class PaginatedIdObjectListBuilderGImplTest extends AbstractBuilderGImplTest {
    @BeforeMethod
    def setUp() {
        entity = new PaginatedIdObjectListGImpl()
        builder = new PaginatedIdObjectListBuilderGImpl(entity: entity)
    }

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
