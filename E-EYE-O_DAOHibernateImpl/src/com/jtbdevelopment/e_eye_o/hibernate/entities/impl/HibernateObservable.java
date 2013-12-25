package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 3/29/13
 * Time: 7:11 PM
 */
//  TODO - tests
@Entity(name = "Observable")
@Audited
@Proxy(lazy = false)
public abstract class HibernateObservable<T extends Observable> extends HibernateAppUserOwnedObject<T> implements Observable {
    protected HibernateObservable() {
    }

    protected HibernateObservable(final T appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    @Override
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    public LocalDateTime getLastObservationTimestamp() {
        return wrapped.getLastObservationTimestamp().withMillisOfSecond(0);
    }

    @Override
    public void setLastObservationTimestamp(final LocalDateTime lastObservationTimestamp) {
        wrapped.setLastObservationTimestamp(lastObservationTimestamp.withMillisOfSecond(0));
    }
}
