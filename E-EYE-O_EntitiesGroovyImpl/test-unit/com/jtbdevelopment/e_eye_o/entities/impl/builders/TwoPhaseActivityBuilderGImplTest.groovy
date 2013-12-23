package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.TwoPhaseActivityGImpl

/**
 * Date: 12/1/13
 * Time: 8:49 PM
 */
class TwoPhaseActivityBuilderGImplTest extends AbstractTwoPhaseActivityBuilderTest {
    @Override
    def createEntity() {
        return new TwoPhaseActivityGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new TwoPhaseActivityBuilderGImpl(entity: entity)
    }
}
