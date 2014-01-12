package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.Observation
import com.jtbdevelopment.e_eye_o.entities.ObservationImpl

/**
 * Date: 12/2/13
 * Time: 6:38 AM
 */
class ObservationBuilderImplTest extends AbstractObservationBuilderTest {
    @Override
    def createEntity() {
        return new ObservationImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new ObservationBuilderImpl((Observation) entity)
    }
}
