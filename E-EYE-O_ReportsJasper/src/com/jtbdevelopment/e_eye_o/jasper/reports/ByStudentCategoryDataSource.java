package com.jtbdevelopment.e_eye_o.jasper.reports;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.LocalDate;
import org.springframework.util.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Date: 8/29/13
 * Time: 6:36 AM
 */
//  TODO - Photos
//  TODO - date filter
public class ByStudentCategoryDataSource implements JRDataSource {
    private final ReadOnlyDAO readOnlyDAO;
    private final AppUser appUser;
    private final Set<ClassList> classListsFilter;
    private final Set<Student> studentsFilter;
    private final Set<ObservationCategory> categoriesFilter;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    private Student currentStudent;
    private ObservationCategory currentCategory;
    private Observation currentObservation;
    private Iterator<Student> studentIterator;
    private Iterator<ObservationCategory> categoryIterator;
    private Iterator<Observation> observationIterator;
    private List<ObservationCategory> sortedCategories;


    public ByStudentCategoryDataSource(final ReadOnlyDAO readOnlyDAO, final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate) {
        this.readOnlyDAO = readOnlyDAO;
        this.appUser = appUser;
        this.classListsFilter = classListsFilter;
        this.studentsFilter = studentsFilter;
        this.categoriesFilter = categoriesFilter;
        this.fromDate = fromDate;
        this.toDate = toDate;
        initialize();
    }

    public void initialize() {
        List<Student> activeEntitiesForUser = new LinkedList<>(readOnlyDAO.getActiveEntitiesForUser(Student.class, appUser));
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
        studentIterator = Collections2.filter(activeEntitiesForUser, new Predicate<Student>() {
            @Override
            public boolean apply(@Nullable Student input) {
                return input != null &&
                        (classListsFilter.isEmpty() || !Sets.intersection(input.getActiveClassLists(), classListsFilter).isEmpty()) &&
                        (studentsFilter.isEmpty() || studentsFilter.contains(input));
            }
        }).iterator();

        sortedCategories = new LinkedList<>(readOnlyDAO.getActiveEntitiesForUser(ObservationCategory.class, appUser));
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
    }

    @Override
    public boolean next() throws JRException {
        return rollObservation() || rollCategory() || rollStudent();
    }

    private boolean rollStudent() {
        if (studentIterator != null && studentIterator.hasNext()) {
            while (studentIterator.hasNext()) {
                currentStudent = studentIterator.next();
                categoryIterator = sortedCategories.iterator();
                if (rollCategory()) return true;
            }
        }
        return false;
    }

    private boolean rollCategory() {
        if (categoryIterator != null && categoryIterator.hasNext()) {
            List<Observation> observations = Collections.emptyList();
            while (observations.isEmpty() && categoryIterator.hasNext()) {
                currentCategory = categoryIterator.next();
                observations = readOnlyDAO.getAllObservationsForEntityAndCategory(currentStudent, currentCategory, fromDate, toDate);
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

    private boolean rollObservation() {
        if (observationIterator != null && observationIterator.hasNext()) {
            currentObservation = observationIterator.next();
            return true;
        }
        return false;
    }

    @Override
    public Object getFieldValue(final JRField jrField) throws JRException {
        String[] parts = StringUtils.split(jrField.getName(), "_");
        Object returnValue = null;
        if (parts.length == 2) {
            try {
                switch (parts[0]) {
                    case "STUDENT":
                        if (currentStudent != null) {
                            returnValue = PropertyUtils.getProperty(currentStudent, parts[1]);
                        }
                        break;
                    case "CATEGORY":
                        if (currentCategory != null) {
                            returnValue = PropertyUtils.getProperty(currentCategory, parts[1]);
                        }
                        break;
                    case "OBSERVATION":
                        if (currentObservation != null) {
                            returnValue = PropertyUtils.getProperty(currentObservation, parts[1]);
                        }
                        break;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        if (returnValue == null) {
            switch (jrField.getName()) {
                case "CATEGORY_description":
                    return "No Category";
                case "CATEGORY_shortName":
                    return "NONE";
            }
        }
        return returnValue != null ? returnValue : "";
    }
}
