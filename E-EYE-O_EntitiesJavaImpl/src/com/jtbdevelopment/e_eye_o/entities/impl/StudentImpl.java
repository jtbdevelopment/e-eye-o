package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/4/12
 * Time: 7:24 PM
 */
public class StudentImpl extends ObservableImpl implements Student {
    private String firstName = "";
    private String lastName = "";
    private Set<ClassList> classLists = new HashSet<>();

    StudentImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public Set<ClassList> getClassLists() {
        return new HashSet<>(classLists);
    }

    @Override
    public Set<ClassList> getActiveClassLists() {
        return new HashSet<>(new HashSet<>(
                Collections2.filter(classLists, new Predicate<ClassList>() {
                    @Override
                    public boolean apply(final ClassList classList) {
                        return (classList != null && !classList.isArchived());
                    }
                })));
    }

    @Override
    public Set<ClassList> getArchivedClassLists() {
        return new HashSet<>(new HashSet<>(
                Collections2.filter(classLists, new Predicate<ClassList>() {
                    @Override
                    public boolean apply(final ClassList classList) {
                        return (classList != null && classList.isArchived());
                    }
                })));
    }

    @Override
    public void setClassLists(final Set<ClassList> classLists) {
        this.classLists = new HashSet<>(classLists);
    }

    @Override
    public void addClassList(final ClassList classList) {
        classLists.add(classList);
    }

    @Override
    public void addClassLists(final Collection<ClassList> classLists) {
        this.classLists.addAll(classLists);
    }

    @Override
    public void removeClassList(final ClassList classList) {
        classLists.remove(classList);
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
    public String getSummaryDescription() {
        return (firstName.trim() + " " + lastName.trim()).trim();
    }
}
