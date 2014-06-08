package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.joda.time.DateTime

/**
 * Date: 12/1/13
 * Time: 3:39 PM
 */
class IdObjectBuilderGImpl<T extends IdObject> implements IdObjectBuilder<T> {
    protected T entity

    @Override
    T build() {
        return entity
    }

    @Override
    IdObjectBuilder<T> withId(final String id) {
        entity.id = id
        return this
    }

    @Override
    IdObjectBuilder<T> withModificationTimestamp(final DateTime modificationTimestamp) {
        entity.modificationTimestamp = modificationTimestamp
        return this
    }
}
