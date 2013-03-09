package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
public class ClassListImpl extends AppUserOwnedObjectImpl implements ClassList {
    private String description = "";

    ClassListImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }


    @Override
    public String getViewableDescription() {
        return description.trim();
    }
}
