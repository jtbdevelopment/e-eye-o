package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.IdObjectImpl

/**
 * Date: 12/1/13
 * Time: 3:57 PM
 */
class IdObjectBuilderImplTest extends AbstractIdObjectBuilderTest {

    static class LocalEntityImpl extends IdObjectImpl {
    }

    @Override
    def createEntity() {
        return new LocalEntityImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new IdObjectBuilderImpl<>((LocalEntityImpl) entity)
    }
}
