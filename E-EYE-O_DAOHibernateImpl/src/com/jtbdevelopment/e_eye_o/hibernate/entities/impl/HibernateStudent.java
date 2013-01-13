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
    public HibernateStudent(final Student student) {
        super(student);
    }

    @Override
    @ManyToMany(targetEntity = HibernateClassList.class)
    public Set<ClassList> getClassLists() {
        return getWrapped().getClassLists();
    }

    @Override
    @Transient
    public Set<ClassList> getActiveClassLists() {
        return getWrapped().getActiveClassLists();
    }

    @Override
    @Transient
    public Set<ClassList> getArchivedClassLists() {
        return getWrapped().getArchivedClassLists();
    }

    @Override
    public Student setClassLists(final Set<ClassList> classLists) {
        return getWrapped().setClassLists(wrap(classLists));
    }

    @Override
    public Student addClassList(final ClassList classList) {
        return getWrapped().addClassList(wrap(classList));
    }

    @Override
    public Student addClassLists(final Collection<ClassList> classLists) {
        return getWrapped().addClassLists(wrap(classLists));
    }

    @Override
    public Student removeClassList(final ClassList classList) {
        return getWrapped().removeClassList(wrap(classList));
    }

    @Override
    @Column(nullable = false, length = Student.MAX_NAME_SIZE)
    public String getFirstName() {
        return getWrapped().getFirstName();
    }

    @Override
    public Student setFirstName(String firstName) {
        getWrapped().setFirstName(firstName);
        return this;
    }

    @Override
    @Column(length = Student.MAX_NAME_SIZE)
    public String getLastName() {
        return getWrapped().getLastName();
    }

    @Override
    public Student setLastName(final String lastName) {
        getWrapped().setLastName(lastName);
        return this;
    }
}
