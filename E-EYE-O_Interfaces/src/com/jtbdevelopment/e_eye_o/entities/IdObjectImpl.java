package com.jtbdevelopment.e_eye_o.entities;


import org.springframework.util.StringUtils;
import sun.plugin.dom.exception.InvalidStateException;

import java.security.InvalidParameterException;

/**
 * Date: 11/28/12
 * Time: 9:08 PM
 */
public abstract class IdObjectImpl implements IdObject {
    protected String id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IdObject)) return false;

        IdObject idObject = (IdObject) o;

        return !(id != null ? !id.equals(idObject.getId()) : idObject.getId() != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public IdObject setId(final String id) {
        validateNonNullValue(id);
        if (StringUtils.hasLength(this.id)) {
            throw new InvalidStateException("Cannot re-assign id after assignment");
        }
        this.id = id;
        return this;
    }

    protected void validateNonNullValue(final Object newValue) {
        if (newValue == null) {
            throw new InvalidParameterException("Cannot assign null");
        }
    }
}
