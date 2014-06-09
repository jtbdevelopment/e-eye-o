package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.Observation
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import org.joda.time.LocalDateTime

/**
 * Date: 12/1/13
 * Time: 3:38 PM
 */
class ObservationBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<Observation> implements ObservationBuilder {
    @Override
    ObservationBuilder withObservationSubject(final com.jtbdevelopment.e_eye_o.entities.Observable observationSubject) {
        entity.observationSubject = observationSubject
        return this
    }

    @Override
    ObservationBuilder withObservationTimestamp(final LocalDateTime observationDate) {
        entity.observationTimestamp = observationDate
        return this
    }

    @Override
    ObservationBuilder withSignificant(final boolean significant) {
        entity.significant = significant
        return this
    }

    @Override
    ObservationBuilder addCategory(final ObservationCategory observationCategory) {
        entity.addCategory(observationCategory)
        return this
    }

    @Override
    ObservationBuilder withCategories(final Set<ObservationCategory> observationCategories) {
        entity.categories = observationCategories
        return this
    }

    @Override
    ObservationBuilder withComment(final String comment) {
        entity.comment = comment
        return this
    }
}
