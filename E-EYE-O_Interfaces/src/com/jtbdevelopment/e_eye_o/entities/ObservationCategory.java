package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectDisplayPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectPreferredDescription;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
@IdObjectPreferredDescription(singular = "Category", plural = "Categories")
@IdObjectDisplayPreferences(defaultPageSize = 10, defaultSortField = "shortName", defaultSortAscending = true,
        viewFieldOrder = {"shortName", "description", "archived", "modificationTimestamp"})
public interface ObservationCategory extends AppUserOwnedObject {

    public final static String OBSERVATION_CATEGORY_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL = "ObservationCategory.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR = "ObservationCategory.shortName" + SHORT_NAME_SIZE_ERROR;
    public final static String OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL = "ObservationCategory.shortName" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String OBSERVATION_CATEGORY_DESCRIPTION_SIZE_ERROR = "ObservationCategory.description" + DESCRIPTION_SIZE_ERROR;

    @NotEmpty(message = OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL)
    @Size(max = MAX_SHORT_NAME_SIZE, message = OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR)
    @IdObjectFieldPreferences(label = "Short Name", width = 5, fieldType = IdObjectFieldPreferences.DisplayFieldType.TEXT, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    String getShortName();

    void setShortName(String shortName);

    @NotEmpty(message = OBSERVATION_CATEGORY_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL)
    @Size(max = MAX_DESCRIPTION_SIZE, message = OBSERVATION_CATEGORY_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldPreferences(label = "Description", width = 30, fieldType = IdObjectFieldPreferences.DisplayFieldType.TEXT, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    String getDescription();

    void setDescription(String description);
}
