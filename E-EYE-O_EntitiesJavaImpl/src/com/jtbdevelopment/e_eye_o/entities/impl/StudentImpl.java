package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
public class StudentImpl extends AppUserOwnedObjectImpl implements Student {
    private String firstName = "";
    private String lastName = "";
    private Set<ClassList> classLists = new HashSet<>();

    StudentImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public Set<ClassList> getClassLists() {
        return Collections.unmodifiableSet(classLists);
    }

    @Override
    public Set<ClassList> getActiveClassLists() {
        return Collections.unmodifiableSet(new HashSet<>(
                Collections2.filter(classLists, new Predicate<ClassList>() {
                    @Override
                    public boolean apply(final ClassList classList) {
                        return (classList != null && !classList.isArchived());
                    }
                })));
    }

    @Override
    public Set<ClassList> getArchivedClassLists() {
        return Collections.unmodifiableSet(new HashSet<>(
                Collections2.filter(classLists, new Predicate<ClassList>() {
                    @Override
                    public boolean apply(final ClassList classList) {
                        return (classList != null && classList.isArchived());
                    }
                })));
    }

    @Override
    public void setClassLists(final Set<ClassList> classLists) {
        this.classLists.clear();
        this.classLists.addAll(classLists);
    }

    @Override
    public Student addClassList(final ClassList classList) {
        classLists.add(classList);
        return this;
    }

    @Override
    public Student addClassLists(final Collection<ClassList> classLists) {
        this.classLists.addAll(classLists);
        return this;
    }

    @Override
    public Student removeClassList(final ClassList classList) {
        classLists.remove(classList);
        return this;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getViewableDescription() {
        return (firstName + " " + lastName).trim();
    }
}
