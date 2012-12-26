package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.validation.NoNullsInCollectionCheck;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
public interface ClassList extends ArchivableAppUserOwnedObject {

    public final static String CLASS_LIST_PHOTOS_CANNOT_BE_NULL_ERROR = "ClassList.photos" + CANNOT_BE_NULL_ERROR;

    public final static String CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR = "ClassList.photos" + CANNOT_CONTAIN_NULL_ERROR;
    public final static String CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR = "ClassList.students" + CANNOT_CONTAIN_NULL_ERROR;
    public final static String CLASS_LIST_STUDENTS_CANNOT_BE_NULL_ERROR = "ClassList.students" + CANNOT_BE_NULL_ERROR;
    public final static String CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR = "ClassList.description" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String CLASS_LIST_DESCRIPTION_SIZE_ERROR = "ClassList.description" + MAX_DESCRIPTION_SIZE;

    @NotEmpty(message = CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_DESCRIPTION_SIZE, message = CLASS_LIST_DESCRIPTION_SIZE_ERROR)
    String getDescription();

    ClassList setDescription(final String description);

    @NotNull(message = CLASS_LIST_STUDENTS_CANNOT_BE_NULL_ERROR)
    @NoNullsInCollectionCheck(message = CLASS_LIST_STUDENTS_CANNOT_CONTAIN_NULL_ERROR)
    Set<Student> getStudents();

    ClassList setStudents(final Set<? extends Student> students);

    ClassList addStudent(final Student student);

    ClassList addStudents(final Collection<? extends Student> students);

    ClassList removeStudent(final Student student);

    @NotNull(message = CLASS_LIST_PHOTOS_CANNOT_BE_NULL_ERROR)
    @NoNullsInCollectionCheck(message = CLASS_LIST_PHOTOS_CANNOT_CONTAIN_NULL_ERROR)
    Set<Photo> getPhotos();

    ClassList setPhotos(final Set<? extends Photo> photos);

    ClassList addPhoto(final Photo photo);

    ClassList addPhotos(final Collection<? extends Photo> photo);

    ClassList removePhoto(final Photo photo);
}
