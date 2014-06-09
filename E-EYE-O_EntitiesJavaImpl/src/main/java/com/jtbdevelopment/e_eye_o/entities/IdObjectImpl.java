package com.jtbdevelopment.e_eye_o.entities;


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
    public void setId(final String id) {
        if (StringUtils.hasLength(this.id) && !this.id.equals(id)) {
            throw new IllegalStateException("Cannot re-assign id after assignment");
        }
        this.id = id;
    }

    @Override
    public DateTime getModificationTimestamp() {
        return modificationTimestamp;
    }

    @Override
    public void setModificationTimestamp(final DateTime modificationTimestamp) {
        this.modificationTimestamp = modificationTimestamp;
    }

    @Override
    public String getSummaryDescription() {
        return id;
    }


}
