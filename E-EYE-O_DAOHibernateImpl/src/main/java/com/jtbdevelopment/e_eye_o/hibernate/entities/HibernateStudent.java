package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;
import org.hibernate.annotations.Proxy;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

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
@Audited
@Proxy(lazy = false)
public class HibernateStudent extends HibernateObservable<Student> implements Student {
    @SuppressWarnings("unused")    // Hibernate
    protected HibernateStudent() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateStudent(final Student student) {
        super(student);
    }

    @Override
    @ManyToMany(targetEntity = HibernateClassList.class)
    @AuditJoinTable()
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
    public void setClassLists(final Set<ClassList> classLists) {
        wrapped.setClassLists(wrap(classLists));
    }

    @Override
    public void addClassList(final ClassList classList) {
        wrapped.addClassList(wrap(classList));
    }

    @Override
    public void addClassLists(final Collection<ClassList> classLists) {
        wrapped.addClassLists(wrap(classLists));
    }

    @Override
    public void removeClassList(final ClassList classList) {
        wrapped.removeClassList(wrap(classList));
    }

    @Override
    @Column(nullable = false, length = Student.MAX_NAME_SIZE)
    public String getFirstName() {
        return wrapped.getFirstName();
    }

    @Override
    public void setFirstName(String firstName) {
        wrapped.setFirstName(firstName);
    }

    @Override
    @Column(length = Student.MAX_NAME_SIZE)
    public String getLastName() {
        return wrapped.getLastName();
    }

    @Override
    public void setLastName(final String lastName) {
        wrapped.setLastName(lastName);
    }
}
