package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategoryImpl;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Date: 11/4/12
 * Time: 9:30 PM
 */
@Entity(name = "ObservationCategory")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"appUser_ID", "shortName"}))
public class HDBObservationCategory extends HDBAppUserOwnedObject<ObservationCategory> implements ObservationCategory {
    @SuppressWarnings("unused")
    protected HDBObservationCategory() {
        super(new ObservationCategoryImpl());
        //  For hibernate
    }

    public HDBObservationCategory(final ObservationCategory observationCategory) {
        super(observationCategory);
    }

    @Override
    @Column(nullable = false, length = IdObject.MAX_SHORT_NAME_SIZE)
    public String getShortName() {
        return wrapped.getShortName();
    }

    @Override
    public ObservationCategory setShortName(final String shortName) {
        wrapped.setShortName(shortName);
        return this;
    }

    @Override
    @Column(nullable = false)
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public ObservationCategory setDescription(final String description) {
        wrapped.setDescription(description);
        return this;
    }
}

