package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Student;

import java.util.Set;

/**
 * Date: 3/9/13
 * Time: 11:20 AM
 */
public interface StudentBuilder extends ObservableBuilder<Student> {
    StudentBuilder withFirstName(final String firstName);

    StudentBuilder withLastName(final String lastName);

    StudentBuilder addClassList(final ClassList classList);

    StudentBuilder withClassLists(final Set<ClassList> classLists);
}
