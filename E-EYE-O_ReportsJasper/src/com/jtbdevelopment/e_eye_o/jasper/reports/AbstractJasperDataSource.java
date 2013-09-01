package com.jtbdevelopment.e_eye_o.jasper.reports;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.LocalDate;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

/**
 * Date: 9/1/13
 * Time: 7:09 PM
 */
public abstract class AbstractJasperDataSource implements JRDataSource {
    protected final ReadOnlyDAO readOnlyDAO;
    protected final AppUser appUser;
    protected final Set<ClassList> classListsFilter;
    protected final Set<Student> studentsFilter;
    protected final Set<ObservationCategory> categoriesFilter;
    protected final LocalDate fromDate;
    protected final LocalDate toDate;
    protected Student currentStudent;
    protected ObservationCategory currentCategory;
    protected Observation currentObservation;
    protected Iterator<Student> studentIterator;
    protected Iterator<ObservationCategory> categoryIterator;
    protected Iterator<Observation> observationIterator;

    abstract protected void initialize();

    public AbstractJasperDataSource(final AppUser appUser, final LocalDate toDate, final Set<ClassList> classListsFilter, final Set<ObservationCategory> categoriesFilter, final ReadOnlyDAO readOnlyDAO, final Set<Student> studentsFilter, final LocalDate fromDate) {
        this.appUser = appUser;
        this.toDate = toDate;
        this.classListsFilter = classListsFilter;
        this.categoriesFilter = categoriesFilter;
        this.readOnlyDAO = readOnlyDAO;
        this.studentsFilter = studentsFilter;
        this.fromDate = fromDate;
        initialize();
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
