package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Date: 11/19/12
 * Time: 10:21 PM
 */
@Entity(name = "AppUserOwnedObject")
public abstract class HibernateAppUserOwnedObject<T extends AppUserOwnedObject> extends HibernateIdObject<T> implements AppUserOwnedObject {
    protected HibernateAppUserOwnedObject() {
    }

    protected HibernateAppUserOwnedObject(final T appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    @Override
    @ManyToOne(targetEntity = HibernateAppUser.class, optional = false)
    public AppUser getAppUser() {
        return wrapped.getAppUser();
    }

    @Override
    public void setAppUser(final AppUser appUser) {
        wrapped.setAppUser(wrap(appUser));
    }

    @Override
    @Column(nullable = false)
    public boolean isArchived() {
        return wrapped.isArchived();
    }

    @Override
    public void setArchived(final boolean archived) {
        wrapped.setArchived(archived);
    }
}
