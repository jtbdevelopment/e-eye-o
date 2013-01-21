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
public class HibernateObservation extends HibernateArchivableAppUserOwnedObject<Observation> implements Observation {
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
    public Observation setObservationTimestamp(final LocalDateTime observationDate) {
        wrapped.setObservationTimestamp(observationDate);
        return this;
    }

    @Override
    @Column(nullable = false)
    public boolean isSignificant() {
        return wrapped.isSignificant();
    }

    @Override
    public Observation setSignificant(final boolean significant) {
        wrapped.setSignificant(significant);
        return this;
    }

    @Override
    @Column(nullable = false)
    public boolean getNeedsFollowUp() {
        return wrapped.getNeedsFollowUp();
    }

    @Override
    public Observation setNeedsFollowUp(final boolean needsFollowUp) {
        wrapped.setNeedsFollowUp(needsFollowUp);
        return this;
    }

    @Override
    public LocalDate getFollowUpReminder() {
        return wrapped.getFollowUpReminder();
    }

    @Override
    public Observation setFollowUpReminder(final LocalDate followUpReminder) {
        wrapped.setFollowUpReminder(followUpReminder);
        return this;
    }

    @Override
    @OneToOne(targetEntity = HibernateObservation.class)
    public Observation getFollowUpObservation() {
        return wrapped.getFollowUpObservation();
    }

    @Override
    public Observation setFollowUpObservation(final Observation followUpObservation) {
        wrapped.setFollowUpObservation(wrap(followUpObservation));
        return this;
    }

    @Override
    @ManyToMany(targetEntity = HibernateObservationCategory.class)
    public Set<ObservationCategory> getCategories() {
        return wrapped.getCategories();
    }

    @Override
    public Observation setCategories(final Set<? extends ObservationCategory> categories) {
        wrapped.setCategories(wrap(categories));
        return this;
    }

    @Override
    public Observation addCategory(final ObservationCategory observationCategory) {
        wrapped.addCategory(wrap(observationCategory));
        return this;
    }

    @Override
    public Observation addCategories(final Collection<? extends ObservationCategory> observationCategories) {
        wrapped.addCategories(wrap(observationCategories));
        return this;
    }

    @Override
    public Observation removeCategory(final ObservationCategory observationCategory) {
        wrapped.removeCategory(observationCategory);
        return this;
    }

    @Override
    @Column(nullable = false, length = Observation.MAX_COMMENT_SIZE)
    public String getComment() {
        return wrapped.getComment();
    }

    @Override
    public Observation setComment(final String comment) {
        wrapped.setComment(comment);
        return this;
    }
}
