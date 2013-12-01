package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.ClassList
import groovy.transform.CompileStatic

/**
 * Date: 11/26/13
 * Time: 6:41 AM
 */
@CompileStatic
class ClassListGImpl extends ObservableGImpl implements ClassList {
    String description = ""

    @Override
    String getSummaryDescription() {
        return description
    }
}
