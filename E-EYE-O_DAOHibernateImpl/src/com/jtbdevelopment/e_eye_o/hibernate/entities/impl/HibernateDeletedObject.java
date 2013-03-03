package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.DeletedObject;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 2/17/13
 * Time: 1:14 PM
 */
@Entity(name = "DeletedObject")
public class HibernateDeletedObject extends HibernateAppUserOwnedObject<DeletedObject> implements DeletedObject {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateDeletedObject() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateDeletedObject(final DeletedObject deletedObject) {
        super(deletedObject);
    }

    @Override
    @Column(nullable = false, updatable = false, unique = true)
    public String getDeletedId() {
        return wrapped.getDeletedId();
    }

    @Override
    public DeletedObject setDeletedId(final String deletedId) {
        wrapped.setDeletedId(deletedId);
        return this;
    }
}