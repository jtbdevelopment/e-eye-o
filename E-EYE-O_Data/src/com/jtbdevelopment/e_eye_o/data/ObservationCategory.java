package com.jtbdevelopment.e_eye_o.data;

import javax.persistence.Entity;

/**
 * Date: 11/4/12
 * Time: 9:30 PM
 */
@Entity
public class ObservationCategory extends IdObject {
    private String categoryShortName = "";
    private String categoryDescription = "";

    public String getCategoryShortName() {
        return categoryShortName;
    }

    public void setCategoryShortName(String categoryShortName) {
        this.categoryShortName = categoryShortName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }
}
