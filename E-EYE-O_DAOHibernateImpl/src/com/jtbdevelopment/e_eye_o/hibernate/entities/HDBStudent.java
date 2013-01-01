package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Photo;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.entities.impl.StudentImpl;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
@Entity(name = "Student")
public class HDBStudent extends HDBArchivableAppUserOwnedObject<Student> implements Student {
    @SuppressWarnings("unused")
    protected HDBStudent() {
        super(new StudentImpl());
        //  For hibernate
    }

    public HDBStudent(final Student student) {
        super(student);
    }

    @Override
    @ManyToMany(targetEntity = HDBClassList.class)
    public Set<ClassList> getClassLists() {
        return wrapped.getClassLists();
    }

    @Override
    @Transient
    public Set<ClassList> getActiveClassLists() {
        return wrapped.getActiveClassLists();
    }

    @Override
    @Transient
    public Set<ClassList> getArchivedClassLists() {
        return wrapped.getArchivedClassLists();
    }

    @Override
    public Student setClassLists(final Set<ClassList> classLists) {
        return wrapped.setClassLists(wrap(classLists));
    }

    @Override
    public Student addClassList(final ClassList classList) {
        return wrapped.addClassList(wrap(classList));
    }

    @Override
    public Student addClassLists(final Collection<ClassList> classLists) {
        return wrapped.addClassLists(wrap(classLists));
    }

    @Override
    public Student removeClassList(final ClassList classList) {
        return wrapped.removeClassList(wrap(classList));
    }

    @Override
    @Column(nullable = false, length = Student.MAX_NAME_SIZE)
    public String getFirstName() {
        return wrapped.getFirstName();
    }

    @Override
    public Student setFirstName(String firstName) {
        wrapped.setFirstName(firstName);
        return this;
    }

    @Override
    @Column(length = Student.MAX_NAME_SIZE)
    public String getLastName() {
        return wrapped.getLastName();
    }

    @Override
    public Student setLastName(final String lastName) {
        wrapped.setLastName(lastName);
        return this;
    }

    @Override
    @OneToOne(targetEntity = HDBPhoto.class)
    public Photo getStudentPhoto() {
        return wrapped.getStudentPhoto();
    }

    @Override
    public Student setStudentPhoto(final Photo studentPhoto) {
        wrapped.setStudentPhoto(wrap(studentPhoto));
        return this;
    }
}
