package com.jtbdevelopment.e_eye_o.data;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
@Entity
public class Observation extends IdObject {
    private Student student;
    private LocalDateTime observationDate;
    private boolean significant = false;
    private Set<ObservationCategory> categories = new HashSet<ObservationCategory>();
    private Set<Photo> photos = new HashSet<Photo>();

    private boolean followUp = false;
    private LocalDate followUpReminder;
    private Observation followUpObservation;

    @ManyToOne(fetch = FetchType.LAZY)
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(LocalDateTime observationDate) {
        this.observationDate = observationDate;
    }

    public boolean isSignificant() {
        return significant;
    }

    public void setSignificant(boolean significant) {
        this.significant = significant;
    }

    @ElementCollection
    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(final Photo photo) {
        this.photos.add(photo);
    }

    public void removePhoto(final Photo photo) {
        this.photos.remove(photo);
    }

    public boolean isFollowUp() {
        return followUp;
    }

    public void setFollowUp(boolean followUp) {
        this.followUp = followUp;
    }

    @Temporal(TemporalType.DATE)
    public LocalDate getFollowUpReminder() {
        return followUpReminder;
    }

    public void setFollowUpReminder(LocalDate followUpReminder) {
        this.followUpReminder = followUpReminder;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public Observation getFollowUpObservation() {
        return followUpObservation;
    }

    public void setFollowUpObservation(final Observation followUpObservation) {
        if( id != null && this.id.equals(followUpObservation.id)) {
            throw new InvalidParameterException("Cannot follow-up observation with itself");
        }
        this.followUpObservation = followUpObservation;
    }

    @ElementCollection
    public Set<ObservationCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<ObservationCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(final ObservationCategory observationCategory) {
        categories.add(observationCategory);
    }

    public void removeCategory(final ObservationCategory observationCategory) {
        categories.remove(observationCategory);
    }
}
