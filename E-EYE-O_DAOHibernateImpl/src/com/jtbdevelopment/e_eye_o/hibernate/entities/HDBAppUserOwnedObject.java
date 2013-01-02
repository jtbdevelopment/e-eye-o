package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Date: 11/19/12
 * Time: 10:21 PM
 */
@Entity(name = "OwnedObject")
public abstract class HDBAppUserOwnedObject<T extends AppUserOwnedObject> extends HDBIdObject<T> implements AppUserOwnedObject {
    protected HDBAppUserOwnedObject() {
    }

    protected HDBAppUserOwnedObject(final T appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    @Override
    @ManyToOne(targetEntity = HDBAppUser.class, optional = false)
    public AppUser getAppUser() {
        return getWrapped().getAppUser();
    }

    @Override
    public T setAppUser(final AppUser appUser) {
        getWrapped().setAppUser(wrap(appUser));
        return (T) this;
    }
}
