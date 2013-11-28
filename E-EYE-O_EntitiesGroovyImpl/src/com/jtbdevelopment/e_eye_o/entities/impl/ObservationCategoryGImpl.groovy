package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory

/**
 * Date: 11/26/13
 * Time: 6:58 AM
 */
class ObservationCategoryGImpl extends AppUserOwnedObjectGImpl implements ObservationCategory {
    String shortName = ""
    String description = ""

    @Override
    String getSummaryDescription() {
        return description.trim()
    }
}
