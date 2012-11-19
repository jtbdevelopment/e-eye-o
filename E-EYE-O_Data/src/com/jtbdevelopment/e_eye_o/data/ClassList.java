package com.jtbdevelopment.e_eye_o.data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 11/17/12
 * Time: 2:05 PM
 */
@Entity
public class ClassList extends IdObject {
    private String description = "";
    private Set<Student> students = new HashSet<Student>();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ElementCollection()
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(final Student student) {
        students.add(student);
    }

    public void removeStudent(final Student student) {
        students.remove(students);
    }
}
