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
    public HibernateObservation(final Observation observation) {
        super(observation);
    }

    @Override
    @ManyToOne(targetEntity = HibernateAppUserOwnedObject.class, optional = false)
    public AppUserOwnedObject getObservationSubject() {
        return getWrapped().getObservationSubject();
    }

    @Override
    public Observation setObservationSubject(final AppUserOwnedObject observationSubject) {
        return getWrapped().setObservationSubject(observationSubject);
    }

    @Override
    @Column(nullable = false)
    public LocalDateTime getObservationTimestamp() {
        return getWrapped().getObservationTimestamp();
    }

    @Override
    public Observation setObservationTimestamp(final LocalDateTime observationDate) {
        getWrapped().setObservationTimestamp(observationDate);
        return this;
    }

    @Override
    @Column(nullable = false)
    public boolean isSignificant() {
        return getWrapped().isSignificant();
    }

    @Override
    public Observation setSignificant(final boolean significant) {
        getWrapped().setSignificant(significant);
        return this;
    }

    @Override
    @Column(nullable = false)
    public boolean getNeedsFollowUp() {
        return getWrapped().getNeedsFollowUp();
    }

    @Override
    public Observation setNeedsFollowUp(final boolean needsFollowUp) {
        getWrapped().setNeedsFollowUp(needsFollowUp);
        return this;
    }

    @Override
    public LocalDate getFollowUpReminder() {
        return getWrapped().getFollowUpReminder();
    }

    @Override
    public Observation setFollowUpReminder(final LocalDate followUpReminder) {
        getWrapped().setFollowUpReminder(followUpReminder);
        return this;
    }

    @Override
    @OneToOne(targetEntity = HibernateObservation.class)
    public Observation getFollowUpObservation() {
        return getWrapped().getFollowUpObservation();
    }

    @Override
    public Observation setFollowUpObservation(final Observation followUpObservation) {
        getWrapped().setFollowUpObservation(wrap(followUpObservation));
        return this;
    }

    @Override
    @ManyToMany(targetEntity = HibernateObservationCategory.class)
    public Set<ObservationCategory> getCategories() {
        return getWrapped().getCategories();
    }

    @Override
    public Observation setCategories(final Set<? extends ObservationCategory> categories) {
        getWrapped().setCategories(wrap(categories));
        return this;
    }

    @Override
    public Observation addCategory(final ObservationCategory observationCategory) {
        getWrapped().addCategory(wrap(observationCategory));
        return this;
    }

    @Override
    public Observation addCategories(final Collection<? extends ObservationCategory> observationCategories) {
        getWrapped().addCategories(wrap(observationCategories));
        return this;
    }

    @Override
    public Observation removeCategory(final ObservationCategory observationCategory) {
        getWrapped().removeCategory(observationCategory);
        return this;
    }

    @Override
    @Column(nullable = false, length = Observation.MAX_COMMENT_SIZE)
    public String getComment() {
        return getWrapped().getComment();
    }

    @Override
    public Observation setComment(final String comment) {
        getWrapped().setComment(comment);
        return this;
    }
}
