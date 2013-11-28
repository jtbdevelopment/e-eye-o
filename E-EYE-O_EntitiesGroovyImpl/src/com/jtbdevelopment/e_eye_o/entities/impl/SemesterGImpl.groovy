package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.Semester
import org.joda.time.LocalDate

/**
 * Date: 11/25/13
 * Time: 8:58 PM
 */
class SemesterGImpl extends AppUserOwnedObjectGImpl implements Semester {
    String description = ""
    LocalDate start
    LocalDate end

    @Override
    String getSummaryDescription() {
        return description
    }
}
