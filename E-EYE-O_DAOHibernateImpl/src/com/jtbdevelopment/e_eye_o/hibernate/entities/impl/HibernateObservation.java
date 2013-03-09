package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
@Entity(name = "Observation")
public class HibernateObservation extends HibernateAppUserOwnedObject<Observation> implements Observation {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateObservation() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateObservation(final Observation observation) {
        super(observation);
    }

    @Override
    @ManyToOne(targetEntity = HibernateAppUserOwnedObject.class, optional = false)
    public AppUserOwnedObject getObservationSubject() {
        return wrapped.getObservationSubject();
    }

    @Override
    public Observation setObservationSubject(final AppUserOwnedObject observationSubject) {
        wrapped.setObservationSubject(observationSubject);
        return this;
    }

    @Override
    @Column(nullable = false)
    public LocalDateTime getObservationTimestamp() {
        return wrapped.getObservationTimestamp();
    }

    @Override
    public void setObservationTimestamp(final LocalDateTime observationDate) {
        wrapped.setObservationTimestamp(observationDate);
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
    @Column(nullable = false)
    public boolean isFollowUpNeeded() {
        return wrapped.isFollowUpNeeded();
    }

    @Override
    public void setFollowUpNeeded(final boolean followUpNeeded) {
        wrapped.setFollowUpNeeded(followUpNeeded);
    }

    @Override
    public LocalDate getFollowUpReminder() {
        return wrapped.getFollowUpReminder();
    }

    @Override
    public void setFollowUpReminder(final LocalDate followUpReminder) {
        wrapped.setFollowUpReminder(followUpReminder);
    }

    @Override
    @OneToOne(targetEntity = HibernateObservation.class)
    public Observation getFollowUpObservation() {
        return wrapped.getFollowUpObservation();
    }

    @Override
    public void setFollowUpObservation(final Observation followUpObservation) {
        wrapped.setFollowUpObservation(wrap(followUpObservation));
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
