package com.jtbdevelopment.e_eye_o.entities

import groovy.transform.CompileStatic
import org.joda.time.LocalDate

/**
 * Date: 11/25/13
 * Time: 8:58 PM
 */
@CompileStatic
class SemesterGImpl extends AppUserOwnedObjectGImpl implements Semester {
    String description = ""
    LocalDate start
    LocalDate end

    @Override
    String getSummaryDescription() {
        return description
    }
}
