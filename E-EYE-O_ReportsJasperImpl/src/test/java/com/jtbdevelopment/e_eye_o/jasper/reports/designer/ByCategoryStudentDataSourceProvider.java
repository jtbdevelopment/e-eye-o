package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.jasper.reports.ByCategoryStudentDataSource;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Date: 8/29/13
 * Time: 8:16 PM
 */
public class ByCategoryStudentDataSourceProvider extends JasperDataSourceProvider {
    private static List<Class<? extends AppUserOwnedObject>> groupBy = Arrays.asList(ObservationCategory.class, Student.class, Observation.class);

    private final ReadOnlyDAO readOnlyDAO;
    private final AppUser appUser;
    private final Set<ClassList> classListsFilter;
    private final Set<Student> studentsFilter;
    private final Set<ObservationCategory> categoriesFilter;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public ByCategoryStudentDataSourceProvider(final ReadOnlyDAO readOnlyDAO, final IdObjectReflectionHelper reflectionHelper, final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate) {
        super(reflectionHelper, groupBy);
        this.readOnlyDAO = readOnlyDAO;
        this.appUser = appUser;
        this.classListsFilter = classListsFilter;
        this.studentsFilter = studentsFilter;
        this.categoriesFilter = categoriesFilter;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public JRDataSource create(JasperReport jasperReport) throws JRException {
        return new ByCategoryStudentDataSource(readOnlyDAO, appUser, classListsFilter, studentsFilter, categoriesFilter, fromDate, toDate);
    }
}
