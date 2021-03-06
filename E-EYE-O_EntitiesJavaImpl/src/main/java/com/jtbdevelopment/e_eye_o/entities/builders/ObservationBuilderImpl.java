package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.joda.time.LocalDateTime;

import java.util.Set;

/**
 * Date: 3/9/13
 * Time: 12:27 PM
 */
public class ObservationBuilderImpl extends AppUserOwnedObjectBuilderImpl<Observation> implements ObservationBuilder {
    public ObservationBuilderImpl(final Observation entity) {
        super(entity);
    }

    @Override
    public ObservationBuilder withObservationSubject(final Observable observationSubject) {
        entity.setObservationSubject(observationSubject);
        return this;
    }

    @Override
    public ObservationBuilder withObservationTimestamp(final LocalDateTime observationDate) {
        entity.setObservationTimestamp(observationDate);
        return this;
    }

    @Override
    public ObservationBuilder withSignificant(final boolean significant) {
        entity.setSignificant(significant);
        return this;
    }

    @Override
    public ObservationBuilder addCategory(final ObservationCategory observationCategory) {
        entity.addCategory(observationCategory);
        return this;
    }

    @Override
    public ObservationBuilder withCategories(final Set<ObservationCategory> observationCategories) {
        entity.setCategories(observationCategories);
        return this;
    }

    @Override
    public ObservationBuilder withComment(final String comment) {
        entity.setComment(comment);
        return this;
    }
}
