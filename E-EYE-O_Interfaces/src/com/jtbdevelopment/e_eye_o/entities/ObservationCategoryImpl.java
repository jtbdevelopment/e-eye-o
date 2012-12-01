package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/4/12
 * Time: 9:30 PM
 */
public class ObservationCategoryImpl extends AppUserOwnedObjectImpl implements ObservationCategory {
    private String shortName = "";
    private String description = "";

    public ObservationCategoryImpl() {
    }

    public ObservationCategoryImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public ObservationCategory setShortName(final String shortName) {
        validateNonNullValue(shortName);
        this.shortName = shortName;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ObservationCategory setDescription(final String description) {
        validateNonNullValue(description);
        this.description = description;
        return this;
    }
}

