package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.Student
import com.jtbdevelopment.e_eye_o.entities.builders.StudentBuilder

/**
 * Date: 12/1/13
 * Time: 3:37 PM
 */
class StudentBuilderGImpl extends ObservableBuilderGImpl<Student> implements StudentBuilder {
    @Override
    StudentBuilder withFirstName(final String firstName) {
        entity.firstName = firstName
        return this
    }

    @Override
    StudentBuilder withLastName(final String lastName) {
        entity.lastName = lastName
        return this
    }

    @Override
    StudentBuilder addClassList(final ClassList classList) {
        entity.addClassList(classList)
        return this
    }

    @Override
    StudentBuilder withClassLists(final Set<ClassList> classLists) {
        entity.classLists = classLists
        return this
    }
}
