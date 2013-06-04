package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectPreferredDescription;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectTableDisplayPreferences;
import com.jtbdevelopment.e_eye_o.entities.validation.NoNullsInCollectionCheck;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
@IdObjectPreferredDescription(singular = "Observation", plural = "Observations")
@IdObjectTableDisplayPreferences(defaultDisplaySize = 25, defaultSortField = "observationTimestamp", defaultSortAscending = false)
public interface Observation extends AppUserOwnedObject {

    public final static String OBSERVATION_COMMENT_CANNOT_BE_BLANK_OR_NULL_ERROR = "Observation.comment" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR = "Observation.categories" + CANNOT_CONTAIN_NULL_ERROR;
    public final static String OBSERVATION_CATEGORIES_CANNOT_BE_NULL_ERROR = "Observation.categories" + CANNOT_BE_NULL_ERROR;
    public final static String OBSERVATION_OBSERVATION_TIMESTAMP_CANNOT_BE_NULL_ERROR = "Observation.observationTimestamp" + CANNOT_BE_NULL_ERROR;
    public final static String OBSERVATION_OBSERVATION_SUBJECT_CANNOT_BE_NULL_ERROR = "Observation.observationSubject" + CANNOT_BE_NULL_ERROR;
    public final static int MAX_COMMENT_SIZE = 5000;
    public final static String OBSERVATION_COMMENT_SIZE_ERROR = "Observation.comment cannot be longer than " + MAX_COMMENT_SIZE + " characters.";

    @NotNull(message = OBSERVATION_OBSERVATION_SUBJECT_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Observation For", uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.SINGLE_SELECT_LIST, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.LEFT)
    Observable getObservationSubject();

    void setObservationSubject(final Observable observationSubject);

    @NotNull(message = OBSERVATION_OBSERVATION_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Observation Time", uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.DATE_TIME, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.MIDDLE)
    LocalDateTime getObservationTimestamp();

    void setObservationTimestamp(final LocalDateTime observationDate);

    @IdObjectFieldPreferences(defautlLabel = "Significant?", uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.CHECKBOX, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.MIDDLE)
    boolean isSignificant();

    void setSignificant(final boolean significant);

    @NotNull(message = OBSERVATION_CATEGORIES_CANNOT_BE_NULL_ERROR)
    @NoNullsInCollectionCheck(message = OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Categories", height = 7, uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.MULTI_SELECT_PICKER, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.MIDDLE)
    Set<ObservationCategory> getCategories();

    void setCategories(final Set<ObservationCategory> categories);

    void addCategory(final ObservationCategory observationCategory);

    void addCategories(final Collection<ObservationCategory> observationCategories);

    void removeCategory(final ObservationCategory observationCategory);

    @NotEmpty(message = OBSERVATION_COMMENT_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_COMMENT_SIZE, message = OBSERVATION_COMMENT_SIZE_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Comment", width = 40, height = 9, uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.MULTI_LINE_TEXT, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.MIDDLE)
    String getComment();

    void setComment(final String comment);
}
