package com.jtbdevelopment.e_eye_o.jasper.reports.designer;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

/**
 * Date: 8/30/13
 * Time: 7:35 PM
 */

public class JasperRunner {
    @Test(enabled = false)
    public void testRun() throws JRException, InterruptedException {
        System.out.println("Out");
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader) cl).getURLs();

        for (URL url : urls) {
            System.out.println(url.getFile());
        }
        InputStream jrxml = JasperRunner.class.getClassLoader().getResourceAsStream("ByStudentByCategory.jrxml");
        JasperDesign design = JRXmlLoader.load(jrxml);
        JasperReport report = JasperCompileManager.compileReport(design);
        DesignerByStudentCategoryDataSourceProvider provider = new DesignerByStudentCategoryDataSourceProvider();
        JasperPrint print = JasperFillManager.fillReport(report, new HashMap<String, Object>(), provider.create(report));
        JasperViewer.viewReport(print);
        Thread.sleep(10000);
        JasperExportManager.exportReportToPdf(print);
    }

}
