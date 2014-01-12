package com.jtbdevelopment.e_eye_o.entities

import groovy.transform.CompileStatic

/**
 * Date: 11/26/13
 * Time: 7:07 AM
 */
@CompileStatic
class StudentGImpl extends ObservableGImpl implements Student {
    Set<ClassList> classLists = new HashSet<>();
    String firstName = ""
    String lastName = ""

    void setClassLists(final Set<ClassList> classLists) {
        this.classLists.clear()
        this.classLists.addAll(classLists)
    }

    Set<ClassList> getClassLists() {
        return new HashSet<>(classLists)
    }

    @Override
    Set<ClassList> getActiveClassLists() {
        return ((classLists.findAll() { ClassList cl -> !cl.archived }) as Set<ClassList>)
    }

    @Override
    Set<ClassList> getArchivedClassLists() {
        return ((classLists.findAll { ClassList cl -> cl.archived }) as Set<ClassList>)
    }

    @Override
    void addClassList(final ClassList classList) {
        this.classLists.add(classList)
    }

    @Override
    void addClassLists(final Collection<ClassList> classLists) {
        this.classLists.addAll(classLists)
    }

    @Override
    void removeClassList(final ClassList classList) {
        this.classLists.remove(classList)
    }

    @Override
    String getSummaryDescription() {
        return (firstName.trim() + " " + lastName.trim()).trim()
    }
}
