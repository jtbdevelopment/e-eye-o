package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import com.jtbdevelopment.e_eye_o.entities.builders.ObservationCategoryBuilder

/**
 * Date: 12/1/13
 * Time: 3:38 PM
 */
class ObservationCategoryBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<ObservationCategory> implements ObservationCategoryBuilder {
    @Override
    ObservationCategoryBuilder withShortName(final String shortName) {
        entity.shortName = shortName
        return this
    }

    @Override
    ObservationCategoryBuilder withDescription(final String description) {
        entity.description = description
        return this
    }
}
