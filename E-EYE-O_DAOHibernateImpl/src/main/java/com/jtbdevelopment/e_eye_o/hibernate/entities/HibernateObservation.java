package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
@Entity(name = "Observation")
@Audited
@Proxy(lazy = false)
public class HibernateObservation extends HibernateAppUserOwnedObject<Observation> implements Observation {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateObservation() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateObservation(final Observation observation) {
        super(observation);
    }

    @Override
    @ManyToOne(targetEntity = HibernateObservable.class, optional = false)
    public Observable getObservationSubject() {
        return wrapped.getObservationSubject();
    }

    @Override
    public void setObservationSubject(final Observable observationSubject) {
        wrapped.setObservationSubject(wrap(observationSubject));
    }

    @Override
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    public LocalDateTime getObservationTimestamp() {
        return wrapped.getObservationTimestamp().withMillisOfSecond(0);
    }

    @Override
    public void setObservationTimestamp(final LocalDateTime observationDate) {
        wrapped.setObservationTimestamp(observationDate.withMillisOfSecond(0));
    }

    @Override
    @Column(nullable = false)
    public boolean isSignificant() {
        return wrapped.isSignificant();
    }

    @Override
    public void setSignificant(final boolean significant) {
        wrapped.setSignificant(significant);
    }

    @Override
    @ManyToMany(targetEntity = HibernateObservationCategory.class)
    public Set<ObservationCategory> getCategories() {
        return wrapped.getCategories();
    }

    @Override
    public void setCategories(final Set<ObservationCategory> categories) {
        wrapped.setCategories(wrap(categories));
    }

    @Override
    public void addCategory(final ObservationCategory observationCategory) {
        wrapped.addCategory(wrap(observationCategory));
    }

    @Override
    public void addCategories(final Collection<ObservationCategory> observationCategories) {
        wrapped.addCategories(wrap(observationCategories));
    }

    @Override
    public void removeCategory(final ObservationCategory observationCategory) {
        wrapped.removeCategory(observationCategory);
    }

    @Override
    @Column(nullable = false, length = Observation.MAX_COMMENT_SIZE)
    public String getComment() {
        return wrapped.getComment();
    }

    @Override
    public void setComment(final String comment) {
        wrapped.setComment(comment);
    }
}
