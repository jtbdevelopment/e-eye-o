package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.Semester;
import com.jtbdevelopment.e_eye_o.entities.builders.SemesterBuilder;
import org.joda.time.LocalDate;

/**
 * Date: 11/11/13
 * Time: 5:49 PM
 */
public class SemesterBuilderImpl extends AppUserOwnedObjectBuilderImpl<Semester> implements SemesterBuilder {
    public SemesterBuilderImpl(final Semester entity) {
        super(entity);
    }

    @Override
    public SemesterBuilder withDescription(final String description) {
        entity.setDescription(description);
        return this;
    }

    @Override
    public SemesterBuilder withStart(final LocalDate start) {
        entity.setStart(start);
        return this;
    }

    @Override
    public SemesterBuilder withEnd(final LocalDate end) {
        entity.setEnd(end);
        return this;
    }
}
