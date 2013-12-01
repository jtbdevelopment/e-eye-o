package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import groovy.transform.CompileStatic
import org.joda.time.DateTime

/**
 * Date: 11/18/13
 * Time: 8:52 PM
 */
@CompileStatic
abstract class IdObjectGImpl implements IdObject {
    String id = null
    DateTime modificationTimestamp = DateTime.now();

    String getId() {
        return id;
    }

    void setId(final String id) {
        if (this.id == null || this.id.size() == 0 || id == this.id) {
            this.id = id;
            return;
        }
        throw new IllegalStateException("Cannot override id once set")
    }

    @Override
    String getSummaryDescription() {
        return id
    }

    @Override
    int hashCode() {
        return (id != null && id.size() > 0) ? id.hashCode() : 0;
    }

    @Override
    boolean equals(def obj) {
        if (this.is(obj)) return true;
        if (obj == null || !(obj instanceof IdObject)) return false;

        return (this.id != null && id.size() > 0 && id.equals(((IdObject) obj).id));
    }
}
