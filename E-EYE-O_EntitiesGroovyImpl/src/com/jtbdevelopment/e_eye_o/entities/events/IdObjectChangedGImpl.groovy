package com.jtbdevelopment.e_eye_o.entities.events

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
}
