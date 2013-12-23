package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.Student
import com.jtbdevelopment.e_eye_o.entities.impl.StudentImpl

/**
 * Date: 12/1/13
 * Time: 9:18 PM
 */
class StudentBuilderImplTest extends AbstractStudentBuilderTest {
    @Override
    def createEntity() {
        return new StudentImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new StudentBuilderImpl((Student) entity)
    }
}
