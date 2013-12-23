package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.impl.ClassListImpl

/**
 * Date: 12/1/13
 * Time: 8:40 PM
 */
class ClassListBuilderImplTest extends AbstractClassListBuilderTest {
    @Override
    def createEntity() {
        return new ClassListImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new ClassListBuilderImpl((ClassList) entity)
    }
}
