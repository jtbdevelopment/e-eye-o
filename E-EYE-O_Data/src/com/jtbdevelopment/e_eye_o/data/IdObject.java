package com.jtbdevelopment.e_eye_o.data;

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
    @GeneratedValue()
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
