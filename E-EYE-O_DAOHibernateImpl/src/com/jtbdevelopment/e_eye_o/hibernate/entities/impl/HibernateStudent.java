package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
@Entity(name = "Student")
public class HibernateStudent extends HibernateArchivableAppUserOwnedObject<Student> implements Student {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateStudent() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateStudent(final Student student) {
        super(student);
    }

    @Override
    @ManyToMany(targetEntity = HibernateClassList.class)
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
        wrapped.setClassLists(wrap(classLists));
        return this;
    }

    @Override
    public Student addClassList(final ClassList classList) {
        wrapped.addClassList(wrap(classList));
        return this;
    }

    @Override
    public Student addClassLists(final Collection<ClassList> classLists) {
        wrapped.addClassLists(wrap(classLists));
        return this;
    }

    @Override
    public Student removeClassList(final ClassList classList) {
        wrapped.removeClassList(wrap(classList));
        return this;
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
}
