package com.jtbdevelopment.e_eye_o.DAO;

import com.jtbdevelopment.e_eye_o.data.ClassList;
import com.jtbdevelopment.e_eye_o.data.Observation;
import com.jtbdevelopment.e_eye_o.data.ObservationCategory;
import com.jtbdevelopment.e_eye_o.data.Student;

import java.util.Map;
import java.util.Set;

/**
 * Provides access to physical storage
 * Date: 11/18/12
 * Time: 12:18 AM
 */
public interface DataProvider {
    public ClassList loadClassList(final String id);
    public Student loadStudent(final String id);
    public Observation loadObservation(final String id);
    public ObservationCategory loadObservationCategory(final String id);

    public Set<ObservationCategory> loadAllObservationCategories();

    public Set<Observation> loadAllObservationsForStudent(final Student student);
    public Set<Observation> loadAllObservationsForStudentId(final String id);
    public Map<Student, Set<Observation>> loadAllObservationsForClassList(final ClassList classList);
    public Map<Student, Set<Observation>> loadAllObservationsForClassListId(final String id);

    public Set<Student> loadAllStudentsForClassList(final ClassList classList);
    public Set<Student> loadAllStudentsForClassListId(final String id);

    public ClassList createClassList(final ClassList classList);
    public ClassList updateClassList(final ClassList classList);
    public void deleteClassList(final ClassList classList);



}
