package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import org.joda.time.LocalDateTime;

/**
 * Date: 3/29/13
 * Time: 7:07 PM
 */
public interface ObservableBuilder<T extends Observable> extends AppUserOwnedObjectBuilder<T> {
    ObservableBuilder<T> withLastObservationTime(final LocalDateTime lastObservationTime);
}
