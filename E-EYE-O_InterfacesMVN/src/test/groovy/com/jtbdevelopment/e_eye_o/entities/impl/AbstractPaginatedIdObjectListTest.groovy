package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.*
import org.jmock.Mockery
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/22/13
 * Time: 1:22 PM
 */
abstract class AbstractPaginatedIdObjectListTest {
    PaginatedIdObjectList impl;

    abstract PaginatedIdObjectList createPaginatedIdObjectList();

    @BeforeMethod
    public void setUp() {
        impl = createPaginatedIdObjectList()
    }

    @Test
    public void testGetEntitiesProtected() {
        assert !impl.entities.is(impl.entities)
    }

    @Test
    public void testMoreAvailable() {
        impl.moreAvailable = true
        assert impl.moreAvailable
        impl.moreAvailable = false
        assert !impl.moreAvailable
    }

    @Test
    public void testPageSize() {
        assert 0 == impl.pageSize
        impl.pageSize = 100
        assert 100 == impl.pageSize
    }

    @Test
    void testCurrentPage() {
        assert 0 == impl.currentPage
        impl.currentPage = 100
        assert 100 == impl.currentPage
    }

    @Test
    void testInitialEntities() {
        assert [] == impl.entities
    }

    @Test
    void testSetEntries() {
        Mockery context = new Mockery();

        def first = [context.mock(ClassList.class), context.mock(Student.class)]
        impl.setEntities(first)
        assert first == impl.entities

        def second = [context.mock(Semester.class), context.mock(Photo.class), context.mock(Observation.class)] as Set
        impl.setEntities(second)
        assert second.toList() == impl.entities
    }
}
