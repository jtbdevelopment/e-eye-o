package com.jtbdevelopment.e_eye_o.entities.utilities;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 3/9/13
 * Time: 11:10 AM
 */
public interface IdObjectBuilder<T extends IdObject> {
    T build();

    IdObjectBuilder<T> setId(final String id);
}
