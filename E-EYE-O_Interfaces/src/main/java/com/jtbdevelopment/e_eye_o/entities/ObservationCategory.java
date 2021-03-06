package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:18 PM
 */
@IdObjectEntitySettings(defaultPageSize = 10, defaultSortField = "shortName", defaultSortAscending = true,
        singular = "Category", plural = "Categories",
        viewFieldOrder = {"shortName", "description", "modificationTimestamp", "archived"},
        editFieldOrder = {"shortName", "description"})
public interface ObservationCategory extends AppUserOwnedObject {

    public final static String OBSERVATION_CATEGORY_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL = "ObservationCategory.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR = "ObservationCategory.shortName" + SHORT_NAME_SIZE_ERROR;
    public final static String OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL = "ObservationCategory.shortName" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String OBSERVATION_CATEGORY_DESCRIPTION_SIZE_ERROR = "ObservationCategory.description" + DESCRIPTION_SIZE_ERROR;

    @NotEmpty(message = OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL)
    @Size(max = MAX_SHORT_NAME_SIZE, message = OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR)
    @IdObjectFieldSettings(label = "Short Name", width = 5, fieldType = IdObjectFieldSettings.DisplayFieldType.TEXT, alignment = IdObjectFieldSettings.DisplayAlignment.LEFT)
    String getShortName();

    void setShortName(String shortName);

    @NotEmpty(message = OBSERVATION_CATEGORY_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL)
    @Size(max = MAX_DESCRIPTION_SIZE, message = OBSERVATION_CATEGORY_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldSettings(label = "Description", width = 30, fieldType = IdObjectFieldSettings.DisplayFieldType.TEXT, alignment = IdObjectFieldSettings.DisplayAlignment.LEFT)
    String getDescription();

    void setDescription(String description);
}
