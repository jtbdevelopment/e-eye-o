package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.impl.ObservationImpl;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:26 PM
 */
@Entity(name = "Observation")
public class HDBObservation extends HDBArchivableAppUserOwnedObject<Observation> implements Observation {
    @SuppressWarnings("unused")
    protected HDBObservation() {
        super(new ObservationImpl());
    }

    public HDBObservation(final Observation observation) {
        super(observation);
    }

    @Override
    public LocalDateTime getObservationTimestamp() {
        return wrapped.getObservationTimestamp();
    }

    @Override
    public Observation setObservationTimestamp(final LocalDateTime observationDate) {
        wrapped.setObservationTimestamp(observationDate);
        return this;
    }

    @Override
    public boolean isSignificant() {
        return wrapped.isSignificant();
    }

    @Override
    public Observation setSignificant(final boolean significant) {
        wrapped.setSignificant(significant);
        return this;
    }

    @Override
    @ManyToMany(targetEntity = HDBPhoto.class)
    public Set<Photo> getPhotos() {
        return wrapped.getPhotos();
    }

    @Override
    public Observation setPhotos(final Set<? extends Photo> photos) {
        wrapped.setPhotos(wrap(photos));
        return this;
    }

    @Override
    public Observation addPhoto(final Photo photo) {
        wrapped.addPhoto(wrap(photo));
        return this;
    }

    @Override
    public Observation addPhotos(final Collection<? extends Photo> photos) {
        wrapped.addPhotos(wrap(photos));
        return this;
    }

    @Override
    public Observation removePhoto(final Photo photo) {
        wrapped.removePhoto(photo);
        return this;
    }

    @Override
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
    @OneToOne(targetEntity = HDBObservation.class)
    public Observation getFollowUpObservation() {
        return wrapped.getFollowUpObservation();
    }

    @Override
    public Observation setFollowUpObservation(final Observation followUpObservation) {
        wrapped.setFollowUpObservation(wrap(followUpObservation));
        return this;
    }

    @Override
    @ManyToMany(targetEntity = HDBObservationCategory.class)
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
    @Column(length = 5000)
    public String getComment() {
        return wrapped.getComment();
    }

    @Override
    public Observation setComment(final String comment) {
        wrapped.setComment(comment);
        return this;
    }
}
