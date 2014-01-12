package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.Observable
import org.joda.time.LocalDateTime

/**
 * Date: 12/1/13
 * Time: 3:38 PM
 */
class ObservableBuilderGImpl<T extends Observable> extends AppUserOwnedObjectBuilderGImpl<T> implements ObservableBuilder<T> {
    @Override
    ObservableBuilder withLastObservationTimestamp(final LocalDateTime lastObservationTimestamp) {
        entity.lastObservationTimestamp = lastObservationTimestamp
        return this
    }
}
