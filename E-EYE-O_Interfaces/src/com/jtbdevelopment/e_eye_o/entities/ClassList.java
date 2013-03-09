package com.jtbdevelopment.e_eye_o.entities;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
public interface ClassList extends AppUserOwnedObject {

    public final static String CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR = "ClassList.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String CLASS_LIST_DESCRIPTION_SIZE_ERROR = "ClassList.description" + MAX_DESCRIPTION_SIZE;

    @NotEmpty(message = CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = CLASS_LIST_DESCRIPTION_SIZE_ERROR)
    String getDescription();

    void setDescription(final String description);

}
