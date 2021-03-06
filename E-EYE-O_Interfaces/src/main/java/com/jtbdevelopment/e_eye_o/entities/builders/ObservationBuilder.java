package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.joda.time.LocalDateTime;

import java.util.Set;

/**
 * Date: 3/9/13
 * Time: 11:23 AM
 */
public interface ObservationBuilder extends AppUserOwnedObjectBuilder<Observation> {
    ObservationBuilder withObservationSubject(final Observable observationSubject);

    ObservationBuilder withObservationTimestamp(final LocalDateTime observationDate);

    ObservationBuilder withSignificant(final boolean significant);

    ObservationBuilder addCategory(final ObservationCategory observationCategory);

    ObservationBuilder withCategories(final Set<ObservationCategory> observationCategories);

    ObservationBuilder withComment(final String comment);
}
