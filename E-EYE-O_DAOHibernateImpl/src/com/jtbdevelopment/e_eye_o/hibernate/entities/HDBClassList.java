package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.ClassListImpl;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
@Entity(name = "ClassList")
public class HDBClassList extends HDBArchivableAppUserOwnedObject<ClassList> implements ClassList {
    @SuppressWarnings("unused")
    protected HDBClassList() {
        super(new ClassListImpl());
    }

    public HDBClassList(final ClassList classList) {
        super(classList);
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public ClassList setDescription(final String description) {
        wrapped.setDescription(description);
        return this;
    }

    @Override
    @ElementCollection(targetClass = HDBStudent.class)
    public Set<Student> getStudents() {
        return wrapped.getStudents();
    }

    public ClassList setStudents(final Set<? extends Student> students) {
        wrapped.setStudents(wrap(students));
        return this;
    }

    @Override
    public ClassList addStudent(final Student student) {
        wrapped.addStudent(wrap(student));
        return this;
    }

    @Override
    public ClassList addStudents(final Collection<? extends Student> students) {
        wrapped.addStudents(wrap(students));
        return this;
    }

    @Override
    public ClassList removeStudent(final Student student) {
        wrapped.removeStudent(student);
        return this;
    }

    @Override
    @ElementCollection(targetClass = HDBPhoto.class)
    public Set<Photo> getPhotos() {
        return wrapped.getPhotos();
    }

    public ClassList setPhotos(final Set<? extends Photo> photos) {
        wrapped.setPhotos(wrap(photos));
        return this;
    }

    @Override
    public ClassList addPhoto(final Photo photo) {
        wrapped.addPhoto(wrap(photo));
        return this;
    }

    @Override
    public ClassList addPhotos(final Collection<? extends Photo> photos) {
        wrapped.addPhotos(wrap(photos));
        return this;
    }

    @Override
    public ClassList removePhoto(final Photo photo) {
        wrapped.removePhoto(photo);
        return this;
    }
}
