package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
@Entity
public class Observation extends ArchivableAppUserOwnedObject {
    private String comment;
    private LocalDateTime observationDate;
    private boolean significant = false;

    private Set<ObservationCategory> categories = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();

    private boolean needsFollowUp = false;
    private LocalDate followUpReminder;
    private Observation followUpObservation;

    @SuppressWarnings("unused")
    protected Observation() {
        //  For hibernate
    }

    public Observation(final AppUser appUser) {
        super(appUser);
    }

    public LocalDateTime getObservationDate() {
        return observationDate;
    }

    public Observation setObservationDate(LocalDateTime observationDate) {
        this.observationDate = observationDate;
        return this;
    }

    public boolean isSignificant() {
        return significant;
    }

    public Observation setSignificant(boolean significant) {
        this.significant = significant;
        return this;
    }

    @ManyToMany
    public Set<Photo> getPhotos() {
        return photos;
    }

    @SuppressWarnings("unused") //  hibernate
    private Observation setPhotos(Set<Photo> photos) {
        this.photos = photos;
        return this;
    }

    public Observation addPhoto(final Photo photo) {
        this.photos.add(photo);
        return this;
    }

    public Observation addPhotos(final Collection<Photo> photos) {
        this.photos.addAll(photos);
        return this;
    }

    public Observation removePhoto(final Photo photo) {
        this.photos.remove(photo);
        return this;
    }

    public boolean getNeedsFollowUp() {
        return needsFollowUp;
    }

    public Observation setNeedsFollowUp(boolean needsFollowUp) {
        this.needsFollowUp = needsFollowUp;
        return this;
    }

    public LocalDate getFollowUpReminder() {
        return followUpReminder;
    }

    public Observation setFollowUpReminder(LocalDate followUpReminder) {
        this.followUpReminder = followUpReminder;
        return this;
    }

    @OneToOne
    public Observation getFollowUpObservation() {
        return followUpObservation;
    }

    public Observation setFollowUpObservation(final Observation followUpObservation) {
        if( id != null && followUpObservation != null && this.id.equals(followUpObservation.id)) {
            throw new InvalidParameterException("Cannot follow-up comment with itself");
        }
        this.followUpObservation = followUpObservation;
        return this;
    }

    @ManyToMany
    public Set<ObservationCategory> getCategories() {
        return categories;
    }

    @SuppressWarnings("unused")  // hibernate
    private void setCategories(Set<ObservationCategory> categories) {
        this.categories = categories;
    }

    public Observation addCategory(final ObservationCategory observationCategory) {
        categories.add(observationCategory);
        return this;
    }

    public Observation addCategories(final Collection<ObservationCategory> observationCategories) {
        categories.addAll(observationCategories);
        return this;
    }

    public Observation removeCategory(final ObservationCategory observationCategory) {
        categories.remove(observationCategory);
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Observation setComment(final String comment) {
        this.comment = comment;
        return this;
    }
}
