package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ClassListGImpl

/**
 * Date: 12/1/13
 * Time: 8:40 PM
 */
class ClassListBuilderGImplTest extends AbstractClassListBuilderTest {
    @Override
    def createEntity() {
        return new ClassListGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new ClassListBuilderGImpl(entity: entity)
    }
}
