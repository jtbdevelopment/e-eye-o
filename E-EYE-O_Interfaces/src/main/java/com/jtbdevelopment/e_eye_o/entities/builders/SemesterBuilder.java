package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.Semester;
import org.joda.time.LocalDate;

/**
 * Date: 11/11/13
 * Time: 3:18 PM
 */
public interface SemesterBuilder extends AppUserOwnedObjectBuilder<Semester> {
    SemesterBuilder withDescription(final String description);

    SemesterBuilder withStart(final LocalDate start);

    SemesterBuilder withEnd(final LocalDate end);
}
