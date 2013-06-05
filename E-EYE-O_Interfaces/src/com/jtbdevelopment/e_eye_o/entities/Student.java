package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectDisplayPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectPreferredDescription;
import com.jtbdevelopment.e_eye_o.entities.validation.NoNullsInCollectionCheck;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.beans.Transient;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:14 PM
 */
@IdObjectPreferredDescription(singular = "Student", plural = "Students")
@IdObjectDisplayPreferences(defaultPageSize = 25, defaultSortField = "lastObservation", defaultSortAscending = true)
public interface Student extends Observable {

    public static final String STUDENT_CLASS_LISTS_CANNOT_BE_NULL = "Student.classLists" + CANNOT_BE_NULL_ERROR;
    public static final String STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL = "Student.classLists" + CANNOT_CONTAIN_NULL_ERROR;
    public static final String STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR = "Student.firstName" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String STUDENT_FIRST_NAME_SIZE_ERROR = "Student.firstName" + NAME_SIZE_ERROR;
    public static final String STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR = "Student.lastName" + CANNOT_BE_NULL_ERROR;
    public static final String STUDENT_LAST_NAME_SIZE_ERROR = "Student.lastName" + NAME_SIZE_ERROR;

    @NotNull(message = STUDENT_CLASS_LISTS_CANNOT_BE_NULL)
    @NoNullsInCollectionCheck(message = STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL)
    @IdObjectFieldPreferences(label = "Classes", height = 4, fieldType = IdObjectFieldPreferences.DisplayFieldType.MULTI_SELECT_PICKER, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    Set<ClassList> getClassLists();

    @Transient
    Set<ClassList> getActiveClassLists();

    @Transient
    Set<ClassList> getArchivedClassLists();

    void setClassLists(final Set<ClassList> classLists);

    void addClassList(final ClassList classList);

    void addClassLists(final Collection<ClassList> classLists);

    void removeClassList(final ClassList classList);

    @NotEmpty(message = STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR)
    @Size(max = MAX_NAME_SIZE, message = STUDENT_FIRST_NAME_SIZE_ERROR)
    @IdObjectFieldPreferences(label = "First Name", width = 15, fieldType = IdObjectFieldPreferences.DisplayFieldType.TEXT, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    String getFirstName();

    void setFirstName(final String firstName);

    @NotNull(message = STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR)
    @Size(max = MAX_NAME_SIZE, message = STUDENT_LAST_NAME_SIZE_ERROR)
    @IdObjectFieldPreferences(label = "Last Name", width = 15, fieldType = IdObjectFieldPreferences.DisplayFieldType.TEXT, alignment = IdObjectFieldPreferences.DisplayAlignment.LEFT)
    String getLastName();

    void setLastName(final String lastName);
}
