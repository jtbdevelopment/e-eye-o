package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
public class ObservationImpl extends ArchivableAppUserOwnedObjectImpl implements Observation {
    private String comment;
    private LocalDateTime observationTimestamp = new LocalDateTime();
    private AppUserOwnedObject observationSubject;
    private boolean significant = false;

    private Set<ObservationCategory> categories = new HashSet<>();

    private boolean needsFollowUp = false;
    private LocalDate followUpReminder;
    private Observation followUpObservation;

    ObservationImpl() {
    }

    ObservationImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public AppUserOwnedObject getObservationSubject() {
        return observationSubject;
    }

    @Override
    public Observation setObservationSubject(final AppUserOwnedObject observationSubject) {
        this.observationSubject = observationSubject;
        return this;
    }

    @Override
    public LocalDateTime getObservationTimestamp() {
        return observationTimestamp;
    }

    @Override
    public Observation setObservationTimestamp(final LocalDateTime observationDate) {
        this.observationTimestamp = observationDate;
        return this;
    }

    @Override
    public boolean isSignificant() {
        return significant;
    }

    @Override
    public Observation setSignificant(final boolean significant) {
        this.significant = significant;
        return this;
    }

    @Override
    public boolean getNeedsFollowUp() {
        return needsFollowUp;
    }

    @Override
    public Observation setNeedsFollowUp(final boolean needsFollowUp) {
        this.needsFollowUp = needsFollowUp;
        return this;
    }

    @Override
    public LocalDate getFollowUpReminder() {
        return followUpReminder;
    }

    @Override
    public Observation setFollowUpReminder(final LocalDate followUpReminder) {
        this.followUpReminder = followUpReminder;
        return this;
    }

    @Override
    public Observation getFollowUpObservation() {
        return followUpObservation;
    }

    @Override
    public Observation setFollowUpObservation(final Observation followUpObservation) {
        this.followUpObservation = followUpObservation;
        return this;
    }

    @Override
    public Set<ObservationCategory> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    @Override
    public Observation setCategories(final Set<? extends ObservationCategory> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        return this;
    }

    @Override
    public Observation addCategory(final ObservationCategory observationCategory) {
        categories.add(observationCategory);
        return this;
    }

    @Override
    public Observation addCategories(final Collection<? extends ObservationCategory> observationCategories) {
        categories.addAll(observationCategories);
        return this;
    }

    @Override
    public Observation removeCategory(final ObservationCategory observationCategory) {
        categories.remove(observationCategory);
        return this;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Observation setComment(final String comment) {
        this.comment = comment;
        return this;
    }
}
