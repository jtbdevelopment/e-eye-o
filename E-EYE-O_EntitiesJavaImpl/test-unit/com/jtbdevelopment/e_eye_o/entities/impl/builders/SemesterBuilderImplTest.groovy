package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.Semester
import com.jtbdevelopment.e_eye_o.entities.impl.SemesterImpl

/**
 * Date: 12/1/13
 * Time: 9:29 PM
 */
class SemesterBuilderImplTest extends AbstractSemesterBuilderTest {
    @Override
    def createEntity() {
        return new SemesterImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new SemesterBuilderImpl((Semester) entity)
    }
}
