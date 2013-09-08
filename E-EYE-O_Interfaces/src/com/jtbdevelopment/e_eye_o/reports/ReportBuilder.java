package com.jtbdevelopment.e_eye_o.reports;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import org.joda.time.LocalDate;

import java.util.Set;

/**
 * Date: 9/1/13
 * Time: 11:21 AM
 */
public interface ReportBuilder {
    /**
     * @param appUser          for user
     * @param classListsFilter - class list filter or empty for all
     * @param studentsFilter   - student filter or empty for all
     * @param categoriesFilter - categories filter or empty for all
     * @param fromDate         - earliest date
     * @param toDate           - latest date
     * @return pdf byte array
     */
    byte[] generateObservationReportByStudentAndCategory(final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate);

    /**
     * @param appUser          for user
     * @param classListsFilter - class list filter or empty for all
     * @param studentsFilter   - student filter or empty for all
     * @param categoriesFilter - categories filter or empty for all
     * @param fromDate         - earliest date
     * @param toDate           - latest date
     * @return pdf byte array
     */
    byte[] generateObservationReportByCategoryAndStudent(final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate);

    byte[] generateObservationStudentSummaryReport(final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate);
}
