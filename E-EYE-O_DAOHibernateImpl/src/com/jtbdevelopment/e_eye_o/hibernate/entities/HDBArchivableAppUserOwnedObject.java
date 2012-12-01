package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Date: 11/19/12
 * Time: 10:40 PM
 */
@MappedSuperclass
public abstract class HDBArchivableAppUserOwnedObject<T extends ArchivableAppUserOwnedObject> extends HDBAppUserOwnedObject<T> implements ArchivableAppUserOwnedObject {
    protected HDBArchivableAppUserOwnedObject(final T wrapped) {
        super(wrapped);
    }

    @Override
    @Column(nullable = false)
    public boolean isArchived() {
        return wrapped.isArchived();
    }

    @Override
    public T setArchived(final boolean archived) {
        wrapped.setArchived(archived);
        return (T) this;
    }
}
