package com.jtbdevelopment.e_eye_o.entities;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
public class ClassListImpl extends ObservableImpl implements ClassList {
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
    public String getSummaryDescription() {
        return description.trim();
    }
}
