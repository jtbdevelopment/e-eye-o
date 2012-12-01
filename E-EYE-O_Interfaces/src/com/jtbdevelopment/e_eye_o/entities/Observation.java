package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
public interface Observation extends ArchivableAppUserOwnedObject {
    LocalDateTime getObservationDate();

    Observation setObservationDate(final LocalDateTime observationDate);

    boolean isSignificant();

    Observation setSignificant(final boolean significant);

    Set<Photo> getPhotos();

    Observation setPhotos(final Set<Photo> photos);

    Observation addPhoto(final Photo photo);

    Observation addPhotos(final Collection<Photo> photos);

    Observation removePhoto(final Photo photo);

    boolean getNeedsFollowUp();

    Observation setNeedsFollowUp(final boolean needsFollowUp);

    LocalDate getFollowUpReminder();

    Observation setFollowUpReminder(final LocalDate followUpReminder);

    Observation getFollowUpObservation();

    Observation setFollowUpObservation(final Observation followUpObservation);

    Set<ObservationCategory> getCategories();

    Observation setCategories(final Set<ObservationCategory> categories);

    Observation addCategory(final ObservationCategory observationCategory);

    Observation addCategories(final Collection<ObservationCategory> observationCategories);

    Observation removeCategory(final ObservationCategory observationCategory);

    String getComment();

    Observation setComment(final String comment);
}
