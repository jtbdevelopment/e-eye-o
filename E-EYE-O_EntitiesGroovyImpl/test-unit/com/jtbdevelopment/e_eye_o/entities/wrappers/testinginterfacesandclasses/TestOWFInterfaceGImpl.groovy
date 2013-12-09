package com.jtbdevelopment.e_eye_o.entities.wrappers.testinginterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.joda.time.DateTime

/**
 * Date: 12/8/13
 * Time: 3:57 PM
 *
 * Dupes
 */
public class TestOWFInterfaceGImpl implements TestOWFInterface {
    private static int idCounter = 0;

    String id = null
    DateTime modificationTimestamp = DateTime.now();

    public TestOWFInterfaceGImpl() {
        setId("" + idCounter++);
    }

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
