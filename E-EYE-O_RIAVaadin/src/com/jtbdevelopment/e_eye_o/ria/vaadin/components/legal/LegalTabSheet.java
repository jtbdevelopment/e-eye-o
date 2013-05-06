package com.jtbdevelopment.e_eye_o.ria.vaadin.components.legal;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Runo;
import org.springframework.stereotype.Component;

/**
 * Date: 5/2/13
 * Time: 8:55 PM
 */
@Component
public class LegalTabSheet extends TabSheet {
    public LegalTabSheet() {
        setSizeFull();
        addStyleName(Runo.TABSHEET_SMALL);
        Panel legalPanel = new Panel();
        legalPanel.setSizeFull();
        legalPanel.addStyleName(Runo.PANEL_LIGHT);
        legalPanel.setContent(new Label(TermsAndConditions.TEXT, ContentMode.HTML));
        addTab(legalPanel).setCaption("Terms of Service");

        legalPanel = new Panel();
        legalPanel.setSizeFull();
        legalPanel.addStyleName(Runo.PANEL_LIGHT);
        legalPanel.setContent(new Label(PrivacyPolicy.TEXT, ContentMode.HTML));
        addTab(legalPanel).setCaption("Privacy Policy");

        legalPanel = new Panel();
        legalPanel.setSizeFull();
        legalPanel.addStyleName(Runo.PANEL_LIGHT);
        legalPanel.setContent(new Label(CookiesPolicy.TEXT, ContentMode.HTML));
        addTab(legalPanel).setCaption("Cookies Policy");
    }
}
