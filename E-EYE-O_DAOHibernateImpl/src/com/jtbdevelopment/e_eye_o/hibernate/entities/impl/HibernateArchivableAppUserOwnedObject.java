package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ArchivableAppUserOwnedObject;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/19/12
 * Time: 10:40 PM
 */
@Entity(name = "ArchivableAppUserOwnedObject")
public abstract class HibernateArchivableAppUserOwnedObject<T extends ArchivableAppUserOwnedObject> extends HibernateAppUserOwnedObject<T> implements ArchivableAppUserOwnedObject {
    protected HibernateArchivableAppUserOwnedObject() {
    }

    protected HibernateArchivableAppUserOwnedObject(final T wrapped) {
        super(wrapped);
    }

    @Override
    @Column(nullable = false)
    public boolean isArchived() {
        return wrapped.isArchived();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T setArchived(final boolean archived) {
        wrapped.setArchived(archived);
        return (T) this;
    }
}
