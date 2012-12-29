package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.validation.NoNullsInCollectionCheck;
import com.jtbdevelopment.e_eye_o.entities.validation.ObservationFollowUpCheck;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
@ObservationFollowUpCheck(message = Observation.OBSERVATION_FOLLOW_UP_OBSERVATION_SELF_REFERENCE_ERROR)
public interface Observation extends ArchivableAppUserOwnedObject {

    public final static String OBSERVATION_FOLLOW_UP_OBSERVATION_SELF_REFERENCE_ERROR = "Observation.followUpObservation cannot refer to itself.";
    public final static String OBSERVATION_COMMENT_CANNOT_BE_BLANK_OR_NULL_ERROR = "Observation.comment" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR = "Observation.categories" + CANNOT_CONTAIN_NULL_ERROR;
    public final static String OBSERVATION_CATEGORIES_CANNOT_BE_NULL_ERROR = "Observation.categories" + CANNOT_BE_NULL_ERROR;
    public final static String OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR = "Observation.photos" + CANNOT_CONTAIN_NULL_ERROR;
    public final static String OBSERVATION_PHOTOS_CANNOT_BE_NULL_ERROR = "Observation.photos" + CANNOT_BE_NULL_ERROR;
    public final static String OBSERVATION_OBSERVATION_TIMESTAMP_CANNOT_BE_NULL_ERROR = "Observation.observationTimestamp" + CANNOT_BE_NULL_ERROR;
    public final static int MAX_COMMENT_SIZE = 5000;
    public final static String OBSERVATION_COMMENT_SIZE_ERROR = "Observation.comment cannot be longer than " + MAX_COMMENT_SIZE + " characters.";

    @NotNull(message = OBSERVATION_OBSERVATION_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    LocalDateTime getObservationTimestamp();

    Observation setObservationTimestamp(final LocalDateTime observationDate);

    boolean isSignificant();

    Observation setSignificant(final boolean significant);

    @NotNull(message = OBSERVATION_PHOTOS_CANNOT_BE_NULL_ERROR)
    @NoNullsInCollectionCheck(message = OBSERVATION_PHOTOS_CANNOT_CONTAIN_NULL_ERROR)
    Set<Photo> getPhotos();

    Observation setPhotos(final Set<? extends Photo> photos);

    Observation addPhoto(final Photo photo);

    Observation addPhotos(final Collection<? extends Photo> photos);

    Observation removePhoto(final Photo photo);

    boolean getNeedsFollowUp();

    Observation setNeedsFollowUp(final boolean needsFollowUp);

    LocalDate getFollowUpReminder();

    Observation setFollowUpReminder(final LocalDate followUpReminder);

    Observation getFollowUpObservation();

    Observation setFollowUpObservation(final Observation followUpObservation);

    @NotNull(message = OBSERVATION_CATEGORIES_CANNOT_BE_NULL_ERROR)
    @NoNullsInCollectionCheck(message = OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR)
    Set<ObservationCategory> getCategories();

    Observation setCategories(final Set<? extends ObservationCategory> categories);

    Observation addCategory(final ObservationCategory observationCategory);

    Observation addCategories(final Collection<? extends ObservationCategory> observationCategories);

    Observation removeCategory(final ObservationCategory observationCategory);

    @NotEmpty(message = OBSERVATION_COMMENT_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_COMMENT_SIZE, message = OBSERVATION_COMMENT_SIZE_ERROR)
    String getComment();

    Observation setComment(final String comment);
}
