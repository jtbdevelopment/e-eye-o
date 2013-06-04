package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectPreferredDescription;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectTableDisplayPreferences;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
@IdObjectPreferredDescription(singular = "Class", plural = "Classes")
@IdObjectTableDisplayPreferences(defaultDisplaySize = 5, defaultSortField = "description", defaultSortAscending = true)
public interface ClassList extends Observable {

    public final static String CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR = "ClassList.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String CLASS_LIST_DESCRIPTION_SIZE_ERROR = "ClassList.description" + MAX_DESCRIPTION_SIZE;

    @NotEmpty(message = CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = CLASS_LIST_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldPreferences(defautlLabel = "Description", width = 40, uiAlignment = IdObjectFieldPreferences.PreferredAlignment.LEFT, uiFieldType = IdObjectFieldPreferences.PreferredUIFieldType.TEXT)
    String getDescription();

    void setDescription(final String description);

}
