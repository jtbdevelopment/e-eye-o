package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.ArchivableAppUserOwnedObject;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
    private ClassList() {
        //  For hibernate
    }

    public ClassList(final AppUser appUser) {
        super(appUser);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ElementCollection()
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(final Student student) {
        students.add(student);
    }

    public void removeStudent(final Student student) {
        students.remove(students);
    }

    @ElementCollection
    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(final Set<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(final Photo photo) {
        photos.add(photo);
    }

    public void removePhoto(final Photo photo) {
        photos.remove(photo);
    }
}
