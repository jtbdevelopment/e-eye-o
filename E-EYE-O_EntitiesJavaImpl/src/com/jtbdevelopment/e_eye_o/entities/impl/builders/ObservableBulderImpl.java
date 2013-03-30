package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.builders.ObservableBuilder;
import org.joda.time.LocalDateTime;

/**
 * Date: 3/29/13
 * Time: 7:06 PM
 */
public abstract class ObservableBulderImpl<T extends Observable> extends AppUserOwnedObjectBuilderImpl<T> implements ObservableBuilder<T> {
    protected ObservableBulderImpl(final T entity) {
        super(entity);
    }

    @Override
    public ObservableBuilder<T> withLastObservationTimestamp(final LocalDateTime lastObservationTimestamp) {
        entity.setLastObservationTimestamp(lastObservationTimestamp);
        return this;
    }
}
