package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.ClassList;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
public class ClassListImpl extends AppUserOwnedObjectImpl implements ClassList, AppUserOwnedObject {
    private String description = "";

    ClassListImpl() {
    }

    ClassListImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ClassList setDescription(final String description) {
        this.description = description;
        return this;
    }
}
