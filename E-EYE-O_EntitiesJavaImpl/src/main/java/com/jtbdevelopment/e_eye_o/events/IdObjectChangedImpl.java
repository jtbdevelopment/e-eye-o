package com.jtbdevelopment.e_eye_o.events;

import com.jtbdevelopment.e_eye_o.entities.IdObject;

/**
 * Date: 3/14/13
 * Time: 5:26 PM
 */
public class IdObjectChangedImpl<T extends IdObject> implements IdObjectChanged<T> {

    private ChangeType changeType;
    private T entity;

    public IdObjectChangedImpl(final ChangeType changeType, final T entity) {
        this.entity = entity;
        this.changeType = changeType;
    }

    @Override
    public ChangeType getChangeType() {
        return changeType;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getEntityType() {
        return (Class<T>) entity.getClass();
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof IdObjectChangedImpl)) return false;

        final IdObjectChangedImpl that = (IdObjectChangedImpl) o;

        if (changeType != that.changeType) return false;
        if (!entity.equals(that.entity)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = changeType.hashCode();
        result = 31 * result + entity.hashCode();
        return result;
    }
}
