package com.jtbdevelopment.e_eye_o.entities;

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
public interface Student extends ArchivableAppUserOwnedObject {

    public static final String STUDENT_CLASS_LISTS_CANNOT_BE_NULL = "Student.classLists" + CANNOT_BE_NULL_ERROR;
    public static final String STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL = "Student.classLists" + CANNOT_CONTAIN_NULL_ERROR;
    public static final String STUDENT_CLASS_LIST_SIZE_ERROR = "Student.classLists must contain at least one entry";
    public static final String STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR = "Student.firstName" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public static final String STUDENT_FIRST_NAME_SIZE_ERROR = "Student.firstName" + NAME_SIZE_ERROR;
    public static final String STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR = "Student.lastName" + CANNOT_BE_NULL_ERROR;
    public static final String STUDENT_LAST_NAME_SIZE_ERROR = "Student.lastName" + NAME_SIZE_ERROR;

    @NotNull(message = STUDENT_CLASS_LISTS_CANNOT_BE_NULL)
    @NoNullsInCollectionCheck(message = STUDENT_CLASS_LISTS_CANNOT_CONTAIN_NULL)
    @Size(min = 1, message = STUDENT_CLASS_LIST_SIZE_ERROR)
    //  TODO - revisit min size = probably not feasible since they could delete only class list a student was in
    Set<ClassList> getClassLists();

    @Transient
    Set<ClassList> getActiveClassLists();

    @Transient
    Set<ClassList> getArchivedClassLists();

    Student setClassLists(final Set<ClassList> classLists);

    Student addClassList(final ClassList classList);

    Student addClassLists(final Collection<ClassList> classLists);

    Student removeClassList(final ClassList classList);

    @NotEmpty(message = STUDENT_FIRST_NAME_CANNOT_BE_NULL_OR_BLANK_ERROR)
    @Size(max = MAX_NAME_SIZE, message = STUDENT_FIRST_NAME_SIZE_ERROR)
    String getFirstName();

    Student setFirstName(final String firstName);

    @NotNull(message = STUDENT_LAST_NAME_CANNOT_BE_NULL_ERROR)
    @Size(max = MAX_NAME_SIZE, message = STUDENT_LAST_NAME_SIZE_ERROR)
    String getLastName();

    Student setLastName(final String lastName);
}
