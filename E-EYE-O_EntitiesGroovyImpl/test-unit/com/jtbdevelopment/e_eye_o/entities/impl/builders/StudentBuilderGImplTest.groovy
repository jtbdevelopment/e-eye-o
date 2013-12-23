package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.StudentGImpl

/**
 * Date: 12/1/13
 * Time: 9:18 PM
 */
class StudentBuilderGImplTest extends AbstractStudentBuilderTest {
    @Override
    def createEntity() {
        return new StudentGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new StudentBuilderGImpl(entity: entity)
    }
}
