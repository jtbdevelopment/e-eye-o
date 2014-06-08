package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.IdObjectGImpl

/**
 * Date: 12/1/13
 * Time: 3:57 PM
 */
class IdObjectBuilderGImplTest extends AbstractIdObjectBuilderTest {

    static class LocalEntityImpl extends IdObjectGImpl {
    }

    @Override
    def createEntity() {
        return new LocalEntityImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new IdObjectBuilderGImpl<>(entity: entity)
    }
}
