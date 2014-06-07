package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivityImpl

/**
 * Date: 12/1/13
 * Time: 8:49 PM
 */
class TwoPhaseActivityBuilderImplTest extends AbstractTwoPhaseActivityBuilderTest {
    @Override
    def createEntity() {
        return new TwoPhaseActivityImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new TwoPhaseActivityBuilderImpl((TwoPhaseActivity) entity)
    }
}
