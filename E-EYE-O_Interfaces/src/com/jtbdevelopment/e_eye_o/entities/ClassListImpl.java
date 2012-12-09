package com.jtbdevelopment.e_eye_o.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
public class ClassListImpl extends ArchivableAppUserOwnedObjectImpl implements ClassList {
    private String description = "";
    private Set<Student> students = new HashSet<>();
    private Set<Photo> photos = new HashSet<>();

    public ClassListImpl() {
    }

    public ClassListImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ClassList setDescription(final String description) {
        validateNonEmptyValue(description);
        this.description = description;
        return this;
    }

    @Override
    public Set<Student> getStudents() {
        return Collections.unmodifiableSet(students);
    }

    @Override
    public ClassList setStudents(final Set<Student> students) {
        validateSameAppUsers(students);
        this.students.clear();
        this.students.addAll(students);
        return this;
    }

    @Override
    public ClassList addStudent(final Student student) {
        validateSameAppUser(student);
        students.add(student);
        return this;
    }

    @Override
    public ClassList removeStudent(final Student student) {
        students.remove(student);
        return this;
    }

    @Override
    public Set<Photo> getPhotos() {
        return Collections.unmodifiableSet(photos);
    }

    @Override
    public ClassList setPhotos(final Set<Photo> photos) {
        validateSameAppUsers(photos);
        this.photos.clear();
        this.photos.addAll(photos);
        return this;
    }

    @Override
    public ClassList addPhoto(final Photo photo) {
        validateSameAppUser(photo);
        photos.add(photo);
        return this;
    }

    @Override
    public ClassList removePhoto(final Photo photo) {
        photos.remove(photo);
        return this;
    }
}
