package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ObservationGImpl

/**
 * Date: 12/2/13
 * Time: 6:38 AM
 */
class ObservationBuilderGImplTest extends AbstractObservationBuilderTest {
    @Override
    def createEntity() {
        return new ObservationGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new ObservationBuilderGImpl(entity: entity)
    }
}
