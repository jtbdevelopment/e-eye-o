package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import groovy.transform.CompileStatic

/**
 * Date: 11/26/13
 * Time: 6:58 AM
 */
@CompileStatic
class ObservationCategoryGImpl extends AppUserOwnedObjectGImpl implements ObservationCategory {
    String shortName = ""
    String description = ""

    @Override
    String getSummaryDescription() {
        return description.trim()
    }
}
