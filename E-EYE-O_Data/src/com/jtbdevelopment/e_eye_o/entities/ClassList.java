package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
@Entity
public class ClassList extends ArchivableAppUserOwnedObject {
    private String description = "";
    private Set<Student> students = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();

    @SuppressWarnings("unused")
    protected ClassList() {
        //  For hibernate
    }

    public ClassList(final AppUser appUser) {
        super(appUser);
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public ClassList setDescription(String description) {
        this.description = description;
        return this;
    }

    @ElementCollection()
    public Set<Student> getStudents() {
        return Collections.unmodifiableSet(students);
    }

    @SuppressWarnings("unused")  // hibernate
    private void setStudents(Set<Student> students) {
        this.students = students;
    }

    public ClassList addStudent(final Student student) {
        students.add(student);
        return this;
    }

    public ClassList removeStudent(final Student student) {
        students.remove(student);
        return this;
    }

    @ElementCollection
    public Set<Photo> getPhotos() {
        return Collections.unmodifiableSet(photos);
    }

    @SuppressWarnings("unused")  // hibernate
    private void setPhotos(final Set<Photo> photos) {
        this.photos = photos;
    }

    public ClassList addPhoto(final Photo photo) {
        photos.add(photo);
        return this;
    }

    public ClassList removePhoto(final Photo photo) {
        photos.remove(photo);
        return this;
    }
}
