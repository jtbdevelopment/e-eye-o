package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.SemesterGImpl

/**
 * Date: 12/1/13
 * Time: 9:29 PM
 */
class SemesterBuilderGImplTest extends AbstractSemesterBuilderTest {
    @Override
    def createEntity() {
        return new SemesterGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new SemesterBuilderGImpl(entity: entity)
    }
}
