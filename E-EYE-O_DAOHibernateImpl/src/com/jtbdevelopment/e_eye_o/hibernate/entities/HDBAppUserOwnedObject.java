package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * Date: 11/19/12
 * Time: 10:21 PM
 */
@MappedSuperclass
public abstract class HDBAppUserOwnedObject<T extends AppUserOwnedObject> extends HDBIdObject<T> implements AppUserOwnedObject {
    protected HDBAppUserOwnedObject(final T appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    @Override
    @ManyToOne(targetEntity = HDBAppUser.class)
    public AppUser getAppUser() {
        return wrapped.getAppUser();
    }

    @Override
    public T setAppUser(final AppUser appUser) {
        wrapped.setAppUser(wrap(appUser));
        return (T) this;
    }
}
