package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.security.InvalidParameterException;
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
    private LocalDateTime observationDate;
    private boolean significant = false;

    private Set<ObservationCategory> categories = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();

    private boolean needsFollowUp = false;
    private LocalDate followUpReminder;
    private Observation followUpObservation;

    public ObservationImpl() {
    }

    public ObservationImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public LocalDateTime getObservationDate() {
        return observationDate;
    }

    @Override
    public Observation setObservationDate(final LocalDateTime observationDate) {
        this.observationDate = observationDate;
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
    public Set<Photo> getPhotos() {
        return Collections.unmodifiableSet(photos);
    }

    @Override
    public Observation setPhotos(Set<Photo> photos) {
        validateSameAppUsers(photos);
        this.photos.clear();
        this.photos.addAll(photos);
        return this;
    }

    @Override
    public Observation addPhoto(final Photo photo) {
        validateSameAppUser(photo);
        this.photos.add(photo);
        return this;
    }

    @Override
    public Observation addPhotos(final Collection<Photo> photos) {
        validateSameAppUsers(photos);
        this.photos.addAll(photos);
        return this;
    }

    @Override
    public Observation removePhoto(final Photo photo) {
        this.photos.remove(photo);
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
        if (getId() != null && followUpObservation != null && this.getId().equals(followUpObservation.getId())) {
            throw new InvalidParameterException("Cannot follow-up observation with itself");
        }
        this.followUpObservation = followUpObservation;
        return this;
    }

    @Override
    public Set<ObservationCategory> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    @Override
    public Observation setCategories(final Set<ObservationCategory> categories) {
        validateSameAppUsers(categories);
        this.categories.clear();
        this.categories.addAll(categories);
        return this;
    }

    @Override
    public Observation addCategory(final ObservationCategory observationCategory) {
        validateSameAppUser(observationCategory);
        categories.add(observationCategory);
        return this;
    }

    @Override
    public Observation addCategories(final Collection<ObservationCategory> observationCategories) {
        validateSameAppUsers(observationCategories);
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
        this.comment = useBlankForNullValue(comment);
        return this;
    }
}
