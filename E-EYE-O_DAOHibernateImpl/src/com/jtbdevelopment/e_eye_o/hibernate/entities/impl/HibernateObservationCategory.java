package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Date: 11/4/12
 * Time: 9:30 PM
 */
@Entity(name = "ObservationCategory")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"appUserID", "shortName"}))
public class HibernateObservationCategory extends HibernateAppUserOwnedObject<ObservationCategory> implements ObservationCategory {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateObservationCategory() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateObservationCategory(final ObservationCategory observationCategory) {
        super(observationCategory);
    }

    //  Duplicate column to allow a unique constraint at table level
    @Column(nullable = false)
    @SuppressWarnings("unused")  // Hibernate
    public String getAppUserID() {
        return wrapped.getAppUser().getId();
    }

    @SuppressWarnings("unused")  // Hibernate
    public void setAppUserID(final String appUserID) {
        //  Do nothing
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
    @Column(nullable = false, length = IdObject.MAX_DESCRIPTION_SIZE)
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public ObservationCategory setDescription(final String description) {
        wrapped.setDescription(description);
        return this;
    }
}

