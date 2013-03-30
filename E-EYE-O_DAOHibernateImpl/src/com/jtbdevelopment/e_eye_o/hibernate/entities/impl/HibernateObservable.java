package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 3/29/13
 * Time: 7:11 PM
 */
//  TODO - tests
@Entity(name = "Observable")
public abstract class HibernateObservable<T extends Observable> extends HibernateAppUserOwnedObject<T> implements Observable {
    protected HibernateObservable() {
    }

    protected HibernateObservable(final T appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    @Override
    @Column(nullable = false)
    public LocalDateTime getLastObservationTimestamp() {
        return wrapped.getLastObservationTimestamp();
    }

    @Override
    public void setLastObservationTimestamp(final LocalDateTime lastObservationTimestamp) {
        wrapped.setLastObservationTimestamp(lastObservationTimestamp);
    }
}
