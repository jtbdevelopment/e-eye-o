package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import com.jtbdevelopment.e_eye_o.entities.ObservationCategoryImpl

/**
 * Date: 12/1/13
 * Time: 8:03 PM
 */
class ObservationCategoryBuilderImplTest extends AbstractObservationCategoryBuilderTest {
    @Override
    def createEntity() {
        return new ObservationCategoryImpl()
    }

    @Override
    def createBuilder(final entity) {
        return new ObservationCategoryBuilderImpl((ObservationCategory) entity)
    }
}
