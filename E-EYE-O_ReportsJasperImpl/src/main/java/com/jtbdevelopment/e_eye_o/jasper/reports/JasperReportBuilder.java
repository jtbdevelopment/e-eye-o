package com.jtbdevelopment.e_eye_o.jasper.reports;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.reports.ReportBuilder;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Date: 9/1/13
 * Time: 11:26 AM
 */
@Component
@SuppressWarnings("unused")
public class JasperReportBuilder implements ReportBuilder {
    private final ReadOnlyDAO readOnlyDAO;

    private final JasperReport byStudentByCategoryReport;
    private final JasperReport byCategoryByStudentReport;
    private final JasperReport summaryReport;

    @Autowired
    public JasperReportBuilder(final ReadOnlyDAO readOnlyDAO) throws JRException {
        this.readOnlyDAO = readOnlyDAO;

        byStudentByCategoryReport = loadReportFromJRXML("ByStudentByCategory.jrxml");
        byCategoryByStudentReport = loadReportFromJRXML("ByCategoryByStudent.jrxml");
        summaryReport = loadReportFromJRXML("ByStudentByCategorySummary.jrxml");
    }

    private JasperReport loadReportFromJRXML(String name) throws JRException {
        InputStream jrxml = JasperReportBuilder.class.getClassLoader().getResourceAsStream(name);
        JasperDesign design = JRXmlLoader.load(jrxml);
        return JasperCompileManager.compileReport(design);
    }

    @Override
    public byte[] generateObservationReportByStudentAndCategory(final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate) {
        try {
            ByStudentCategoryDataSource dataSource = new ByStudentCategoryDataSource(readOnlyDAO, appUser, classListsFilter, studentsFilter, categoriesFilter, fromDate, toDate);
            JasperPrint report = JasperFillManager.fillReport(byStudentByCategoryReport, new HashMap<String, Object>(), dataSource);
            return JasperExportManager.exportReportToPdf(report);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] generateObservationReportByCategoryAndStudent(final AppUser appUser, final Set<ClassList> classListsFilter, final Set<Student> studentsFilter, final Set<ObservationCategory> categoriesFilter, final LocalDate fromDate, final LocalDate toDate) {
        try {
            ByCategoryStudentDataSource dataSource = new ByCategoryStudentDataSource(readOnlyDAO, appUser, classListsFilter, studentsFilter, categoriesFilter, fromDate, toDate);
            JasperPrint report = JasperFillManager.fillReport(byCategoryByStudentReport, new HashMap<String, Object>(), dataSource);
            return JasperExportManager.exportReportToPdf(report);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] generateObservationStudentSummaryReport(AppUser appUser, Set<ClassList> classListsFilter, Set<Student> studentsFilter, Set<ObservationCategory> categoriesFilter, LocalDate fromDate, LocalDate toDate) {
        try {
            ByStudentCategoryDataSource dataSource = new ByStudentCategoryDataSource(readOnlyDAO, appUser, classListsFilter, studentsFilter, categoriesFilter, fromDate, toDate);
            JasperPrint report = JasperFillManager.fillReport(summaryReport, new HashMap<String, Object>(), dataSource);
            return JasperExportManager.exportReportToPdf(report);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

}
