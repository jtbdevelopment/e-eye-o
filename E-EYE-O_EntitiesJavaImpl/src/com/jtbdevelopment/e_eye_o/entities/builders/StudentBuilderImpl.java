package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;

import java.util.Set;

/**
 * Date: 3/9/13
 * Time: 12:16 PM
 */
public class StudentBuilderImpl extends ObservableBulderImpl<Student> implements StudentBuilder {
    public StudentBuilderImpl(final Student entity) {
        super(entity);
    }

    @Override
    public StudentBuilder withFirstName(final String firstName) {
        entity.setFirstName(firstName);
        return this;
    }

    @Override
    public StudentBuilder withLastName(final String lastName) {
        entity.setLastName(lastName);
        return this;
    }

    @Override
    public StudentBuilder addClassList(final ClassList classList) {
        entity.addClassList(classList);
        return this;
    }

    @Override
    public StudentBuilder withClassLists(final Set<ClassList> classLists) {
        entity.setClassLists(classLists);
        return this;
    }
}
