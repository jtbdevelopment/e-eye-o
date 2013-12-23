package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.ObservationCategoryGImpl

/**
 * Date: 12/1/13
 * Time: 8:03 PM
 */
class ObservationCategoryBuilderGImplTest extends AbstractObservationCategoryBuilderTest {
    @Override
    def createEntity() {
        return new ObservationCategoryGImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new ObservationCategoryBuilderGImpl(entity: entity)
    }
}
