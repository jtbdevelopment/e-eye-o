package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.legal.LegalTabSheet;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.legal.SafetyTips;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 5/2/13
 * Time: 9:05 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HelpWorkArea extends CustomComponent {
    @Autowired
    LegalTabSheet legalTabSheet;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();

        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(Runo.TABSHEET_SMALL);

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName(Runo.PANEL_LIGHT);
        //  TODO - help text
        panel.setContent(new Label("<H1>Coming Eventually</H1>", ContentMode.HTML));
        tabSheet.addTab(panel).setCaption("General Help");

        panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName(Runo.PANEL_LIGHT);
        panel.setContent(new Label(SafetyTips.TEXT, ContentMode.HTML));
        tabSheet.addTab(panel).setCaption("Safety Tips");
        tabSheet.addTab(legalTabSheet).setCaption("Legal Stuff");

        setCompositionRoot(tabSheet);
    }
}
