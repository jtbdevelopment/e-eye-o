package com.jtbdevelopment.e_eye_o.jasper.reports;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import net.sf.jasperreports.engine.JRException;
import org.joda.time.LocalDate;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Date: 8/29/13
 * Time: 6:36 AM
 */
//  TODO - Photos
//  TODO - use pagination?
public class ByCategoryStudentDataSource extends AbstractJasperDataSource {
    private List<Student> sortedStudents;

    public ByCategoryStudentDataSource(final ReadOnlyDAO readOnlyDAO, final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate) {
        super(appUser, toDate, classListsFilter, categoriesFilter, readOnlyDAO, studentsFilter, fromDate);
    }

    public void initialize() {
        List<ObservationCategory> sortedCategories = new LinkedList<>(readOnlyDAO.getActiveEntitiesForUser(ObservationCategory.class, appUser, 0, 0));
        Collections.sort(sortedCategories, new Comparator<ObservationCategory>() {
            @Override
            public int compare(final ObservationCategory o1, final ObservationCategory o2) {
                int result = o1.getDescription().compareTo(o2.getDescription());
                if (result == 0) {
                    result = o1.getId().compareTo(o2.getId());
                }

                return result;
            }
        });
        sortedCategories.add(0, null);
        sortedCategories = new LinkedList<>(Collections2.filter(sortedCategories, new Predicate<ObservationCategory>() {
            @Override
            public boolean apply(@Nullable ObservationCategory input) {
                return categoriesFilter.isEmpty() || categoriesFilter.contains(input);
            }
        }));
        categoryIterator = sortedCategories.iterator();

        List<Student> activeEntitiesForUser = new LinkedList<>(readOnlyDAO.getActiveEntitiesForUser(Student.class, appUser, 0, 0));
        Collections.sort(activeEntitiesForUser, new Comparator<Student>() {
            @Override
            public int compare(final Student o1, final Student o2) {
                int result = o1.getLastName().compareTo(o2.getLastName());
                if (result == 0) {
                    result = o1.getFirstName().compareTo(o2.getFirstName());
                }
                if (result == 0) {
                    result = o1.getId().compareTo(o2.getId());
                }

                return result;
            }
        });
        sortedStudents = new LinkedList<>(Collections2.filter(activeEntitiesForUser, new Predicate<Student>() {
            @Override
            public boolean apply(@Nullable Student input) {
                return input != null &&
                        (classListsFilter.isEmpty() || !Sets.intersection(input.getActiveClassLists(), classListsFilter).isEmpty()) &&
                        (studentsFilter.isEmpty() || studentsFilter.contains(input));
            }
        }));
    }

    @Override
    public boolean next() throws JRException {
        return rollObservation() || rollStudent() || rollCategory();
    }

    private boolean rollStudent() {
        if (studentIterator != null && studentIterator.hasNext()) {
            List<Observation> observations = new LinkedList<>();
            while (observations.isEmpty() && studentIterator.hasNext()) {
                currentStudent = studentIterator.next();
                observations.addAll(readOnlyDAO.getAllObservationsForEntityAndCategory(currentStudent, currentCategory, fromDate, toDate));
            }
            if (!observations.isEmpty()) {
                Collections.sort(observations, new Comparator<Observation>() {
                    @Override
                    public int compare(final Observation o1, final Observation o2) {
                        int result = o1.getObservationTimestamp().compareTo(o2.getObservationTimestamp());
                        if (result == 0) {
                            result = o1.getId().compareTo(o2.getId());
                        }
                        return result;
                    }
                });
                observationIterator = observations.iterator();
                return rollObservation();
            }
        }
        return false;
    }

    private boolean rollCategory() {
        if (categoryIterator != null && categoryIterator.hasNext()) {
            while (categoryIterator.hasNext()) {
                currentCategory = categoryIterator.next();
                studentIterator = sortedStudents.iterator();
                if (rollStudent()) return true;
            }
        }

        return false;
    }

    private boolean rollObservation() {
        if (observationIterator != null && observationIterator.hasNext()) {
            currentObservation = observationIterator.next();
            return true;
        }
        return false;
    }

}
