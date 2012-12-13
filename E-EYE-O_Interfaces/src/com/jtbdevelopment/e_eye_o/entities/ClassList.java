package com.jtbdevelopment.e_eye_o.entities;

import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
public interface ClassList extends ArchivableAppUserOwnedObject {
    String getDescription();

    ClassList setDescription(final String description);

    Set<Student> getStudents();

    ClassList setStudents(final Set<? extends Student> students);

    ClassList addStudent(final Student student);

    ClassList addStudents(final Collection<? extends Student> students);

    ClassList removeStudent(final Student student);

    Set<Photo> getPhotos();

    ClassList setPhotos(final Set<? extends Photo> photos);

    ClassList addPhoto(final Photo photo);

    ClassList addPhotos(final Collection<? extends Photo> photo);

    ClassList removePhoto(final Photo photo);
}
