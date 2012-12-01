package com.jtbdevelopment.e_eye_o.entities;

import java.util.Set;

/**
 * Date: 11/25/12
 * Time: 3:15 PM
 */
public interface ClassList extends ArchivableAppUserOwnedObject {
    String getDescription();

    ClassList setDescription(final String description);

    Set<Student> getStudents();

    ClassList setStudents(final Set<Student> students);

    ClassList addStudent(final Student student);

    ClassList removeStudent(final Student student);

    Set<Photo> getPhotos();

    ClassList setPhotos(final Set<Photo> photos);

    ClassList addPhoto(final Photo photo);

    ClassList removePhoto(final Photo photo);
}
