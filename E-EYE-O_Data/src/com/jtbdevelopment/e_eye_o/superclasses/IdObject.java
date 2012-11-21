package com.jtbdevelopment.e_eye_o.superclasses;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Date: 11/18/12
 * Time: 12:45 AM
 */
@MappedSuperclass
public abstract class IdObject {
    protected String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdObject idObject = (IdObject) o;

        if (id != null ? !id.equals(idObject.id) : idObject.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    private void setId(final String id) {
        this.id = id;
    }
}
