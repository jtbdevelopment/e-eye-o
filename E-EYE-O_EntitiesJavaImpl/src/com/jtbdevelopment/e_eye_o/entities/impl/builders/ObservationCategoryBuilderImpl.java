package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.builders.ObservationCategoryBuilder;

/**
 * Date: 3/9/13
 * Time: 12:24 PM
 */
public class ObservationCategoryBuilderImpl extends AppUserOwnedObjectBuilderImpl<ObservationCategory> implements ObservationCategoryBuilder {
    public ObservationCategoryBuilderImpl(final ObservationCategory entity) {
        super(entity);
    }

    @Override
    public ObservationCategoryBuilder withShortName(final String shortName) {
        entity.setShortName(shortName);
        return this;
    }

    @Override
    public ObservationCategoryBuilder withDescription(final String description) {
        entity.setDescription(description);
        return this;
    }
}
