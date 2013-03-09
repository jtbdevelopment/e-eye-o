package com.jtbdevelopment.e_eye_o.entities.impl;


import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;

/**
 * Date: 11/28/12
 * Time: 9:08 PM
 */
public abstract class IdObjectImpl implements IdObject {
    protected String id;
    private DateTime modificationTimestamp = new DateTime();

    protected IdObjectImpl() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof IdObject)) return false;

        IdObject idObject = (IdObject) o;

        return (id != null && id.equals(idObject.getId()));

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
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T setId(final String id) {
        if (StringUtils.hasLength(this.id) && !this.id.equals(id)) {
            throw new IllegalStateException("Cannot re-assign id after assignment");
        }
        this.id = id;
        return (T) this;
    }

    @Override
    public DateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends IdObject> T setModificationTimestamp(final DateTime modificationTimestamp) {
        this.modificationTimestamp = modificationTimestamp;
        return (T) this;
    }

    @Override
    public String getViewableDescription() {
        return id;
    }


}
