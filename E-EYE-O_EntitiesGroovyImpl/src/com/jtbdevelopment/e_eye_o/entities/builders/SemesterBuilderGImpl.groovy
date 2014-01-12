package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.Semester
import org.joda.time.LocalDate

/**
 * Date: 12/1/13
 * Time: 3:36 PM
 */
class SemesterBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<Semester> implements SemesterBuilder {
    @Override
    SemesterBuilder withDescription(final String description) {
        entity.description = description
        return this
    }

    @Override
    SemesterBuilder withStart(final LocalDate start) {
        entity.start = start
        return this
    }

    @Override
    SemesterBuilder withEnd(final LocalDate end) {
        entity.end = end
        return this
    }
}
