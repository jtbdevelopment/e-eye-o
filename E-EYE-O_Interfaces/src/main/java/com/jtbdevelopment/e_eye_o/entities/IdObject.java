package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;
import java.beans.Transient;

/**
 * Date: 11/25/12
 * Time: 3:09 PM
 */
public interface IdObject {
    //  Standard defaults
    public final static LocalDateTime UNINITIALISED_LOCAL_DATE_TIME = new LocalDateTime(2000, 1, 1, 0, 0, 0);
    //  Standard sizes
    public final static int MAX_SHORT_NAME_SIZE = 10;
    public final static int MAX_DESCRIPTION_SIZE = 50;
    public final static int MAX_NAME_SIZE = 50;
    //  Standard error message building blocks
    public final static String CANNOT_BE_NULL_ERROR = " cannot be null.";
    public final static String CANNOT_CONTAIN_NULL_ERROR = " cannot contain null.";
    public final static String DESCRIPTION_SIZE_ERROR = " must be " + MAX_DESCRIPTION_SIZE + " characters or less.";
    public final static String SHORT_NAME_SIZE_ERROR = " must be " + MAX_SHORT_NAME_SIZE + " characters or less.";
    public final static String NAME_SIZE_ERROR = " must be " + MAX_NAME_SIZE + " characters or less.";
    public final static String CANNOT_BE_BLANK_OR_NULL_ERROR = " cannot be blank or null.";

    public final static String ID_OBJECT_ID_MAY_NOT_BE_EMPTY_ERROR = "IdObject.id" + CANNOT_BE_BLANK_OR_NULL_ERROR;

    @NotEmpty(message = ID_OBJECT_ID_MAY_NOT_BE_EMPTY_ERROR)
    @IdObjectFieldSettings(editableBy = IdObjectFieldSettings.EditableBy.NONE)
    String getId();

    void setId(final String id);

    @NotNull
    @IdObjectFieldSettings(editableBy = IdObjectFieldSettings.EditableBy.NONE, label = "Last Update", alignment = IdObjectFieldSettings.DisplayAlignment.CENTER)
    DateTime getModificationTimestamp();

    void setModificationTimestamp(final DateTime modificationTimestamp);

    @Transient
    String getSummaryDescription();
}
