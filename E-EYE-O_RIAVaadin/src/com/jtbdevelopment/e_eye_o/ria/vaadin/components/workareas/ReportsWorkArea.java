package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import com.jtbdevelopment.e_eye_o.entities.Student;
import com.jtbdevelopment.e_eye_o.reports.ReportBuilder;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.Collections;

/**
 * Date: 5/7/13
 * Time: 11:26 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - everything
public class ReportsWorkArea extends CustomComponent {
    @Autowired
    private ReportBuilder reportBuilder;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);
        setCompositionRoot(mainLayout);

        Button generate = new Button("Generate");
        mainLayout.addComponent(generate);


        generate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AppUser user = getUI().getSession().getAttribute(AppUser.class);
                final byte[] pdf = reportBuilder.generateObservationReportByStudentAndCategory(user, Collections.<ClassList>emptySet(), Collections.<Student>emptySet(), Collections.<ObservationCategory>emptySet(), new LocalDate().minusYears(2), new LocalDate());
                BrowserFrame report = new BrowserFrame(null, new ConnectorResource() {
                    @Override
                    public String getMIMEType() {
                        return "application/pdf";
                    }

                    @Override
                    public DownloadStream getStream() {
                        return new DownloadStream(new ByteArrayInputStream(pdf), getMIMEType(), getFilename());
                    }

                    @Override
                    public String getFilename() {
                        return "Report" + new LocalDateTime().toString("yyyyMMddHHmmss") + ".pdf";
                    }
                });

                Window window = new Window();
                VerticalLayout mainLayout = new VerticalLayout();
                window.setContent(mainLayout);
                window.setResizable(true);
                window.setSizeFull();
                window.center();
                mainLayout.setSizeFull();
                report.setSizeFull();
                mainLayout.addComponent(report);
                window.addStyleName(Runo.WINDOW_DIALOG);
                window.setModal(true);
                window.setCaption("Report Contents");
                getUI().addWindow(window);
            }
        });

    }
}
