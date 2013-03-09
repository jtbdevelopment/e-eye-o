package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

/**
 * Date: 11/4/12
 * Time: 9:30 PM
 */
public class ObservationCategoryImpl extends AppUserOwnedObjectImpl implements ObservationCategory {
    private String shortName = "";
    private String description = "";

    ObservationCategoryImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(final String shortName) {
        this.shortName = shortName;
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

