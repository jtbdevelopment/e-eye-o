package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.builders.IdObjectBuilder;
import org.joda.time.DateTime;

/**
 * Date: 3/9/13
 * Time: 11:36 AM
 */
public class IdObjectBuilderImpl<T extends IdObject> implements IdObjectBuilder<T> {
    protected final T entity;

    public IdObjectBuilderImpl(final T entity) {
        this.entity = entity;
    }

    @Override
    public T build() {
        return entity;
    }

    @Override
    public IdObjectBuilder<T> withId(final String id) {
        entity.setId(id);
        return this;
    }

    @Override
    public IdObjectBuilder<T> withModificationTimestamp(final DateTime modificationTimestamp) {
        entity.setModificationTimestamp(modificationTimestamp);
        return this;
    }
}
