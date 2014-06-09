package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

/**
 * Date: 3/9/13
 * Time: 11:16 AM
 */
public interface ObservationCategoryBuilder extends AppUserOwnedObjectBuilder<ObservationCategory> {
    ObservationCategoryBuilder withShortName(final String shortName);

    ObservationCategoryBuilder withDescription(final String description);
}
