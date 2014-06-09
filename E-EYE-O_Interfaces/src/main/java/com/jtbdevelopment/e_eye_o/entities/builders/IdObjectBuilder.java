package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.joda.time.DateTime;

/**
 * Date: 3/9/13
 * Time: 11:10 AM
 */
public interface IdObjectBuilder<T extends IdObject> {
    T build();

    IdObjectBuilder<T> withId(final String id);

    IdObjectBuilder<T> withModificationTimestamp(final DateTime modificationTimestamp);
}
