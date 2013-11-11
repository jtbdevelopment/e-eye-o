package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/11/13
 * Time: 3:07 PM
 */
@IdObjectEntitySettings(defaultPageSize = 5, defaultSortField = "description", defaultSortAscending = true,
        singular = "Semester", plural = "Semesters",
        viewFieldOrder = {"description", "start", "end", "modificationTimestamp", "archived"},
        editFieldOrder = {"description", IdObjectEntitySettings.SECTION_BREAK, "start", "end"})
public interface Semester extends AppUserOwnedObject {

    public final static String SEMESTER_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR = "Semester.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String SEMESTER_DESCRIPTION_SIZE_ERROR = "Semester.description" + MAX_DESCRIPTION_SIZE;
    public final static String SEMESTER_START_CANNOT_BE_NULL_ERROR = "Semester.start" + CANNOT_BE_NULL_ERROR;
    public final static String SEMESTER_END_CANNOT_BE_NULL_ERROR = "Semester.end" + CANNOT_BE_NULL_ERROR;

    @NotEmpty(message = SEMESTER_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = SEMESTER_DESCRIPTION_SIZE_ERROR)
    @IdObjectFieldSettings(label = "Description", width = 40, alignment = IdObjectFieldSettings.DisplayAlignment.LEFT, fieldType = IdObjectFieldSettings.DisplayFieldType.TEXT)
    String getDescription();

    void setDescription(final String description);

    @NotNull(message = SEMESTER_START_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldSettings(label = "Start", alignment = IdObjectFieldSettings.DisplayAlignment.LEFT, fieldType = IdObjectFieldSettings.DisplayFieldType.LOCAL_DATE)
    LocalDate getStart();

    void setStart(final LocalDate start);

    @NotNull(message = SEMESTER_END_CANNOT_BE_NULL_ERROR)
    @IdObjectFieldSettings(label = "End", alignment = IdObjectFieldSettings.DisplayAlignment.LEFT, fieldType = IdObjectFieldSettings.DisplayFieldType.LOCAL_DATE)
    LocalDate getEnd();

    void setEnd(final LocalDate end);
}
