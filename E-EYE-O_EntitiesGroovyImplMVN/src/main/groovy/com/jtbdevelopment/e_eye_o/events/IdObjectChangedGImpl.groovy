package com.jtbdevelopment.e_eye_o.events

import com.jtbdevelopment.e_eye_o.entities.IdObject
import groovy.transform.CompileStatic

/**
 * Date: 12/7/13
 * Time: 9:32 PM
 */
@CompileStatic
class IdObjectChangedGImpl<T extends IdObject> implements IdObjectChanged<T> {

    IdObjectChanged.ChangeType changeType;
    T entity;

    @Override
    Class<T> getEntityType() {
        return (Class<T>) entity.class
    }

    @Override
    boolean equals(final o) {
        if (this.is(o)) return true
        if (!(o instanceof IdObjectChangedGImpl)) return false

        final IdObjectChangedGImpl that = (IdObjectChangedGImpl) o

        if (changeType != that.changeType) return false
        if (entity != that.entity) return false

        return true
    }

    @Override
    int hashCode() {
        int result
        result = changeType.hashCode()
        result = 31 * result + entity.hashCode()
        return result
    }
}
