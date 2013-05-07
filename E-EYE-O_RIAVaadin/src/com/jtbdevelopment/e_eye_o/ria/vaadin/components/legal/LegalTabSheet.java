package com.jtbdevelopment.e_eye_o.ria.vaadin.components.legal;

import com.jtbdevelopment.e_eye_o.helpandlegal.CookiesPolicy;
import com.jtbdevelopment.e_eye_o.helpandlegal.PrivacyPolicy;
import com.jtbdevelopment.e_eye_o.helpandlegal.TermsAndConditions;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Date: 5/2/13
 * Time: 8:55 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LegalTabSheet extends TabSheet {
    @Autowired
    private CookiesPolicy cookiesPolicy;
    @Autowired
    private PrivacyPolicy privacyPolicy;
    @Autowired
    private TermsAndConditions termsAndConditions;

    private int tabCount = 0;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();
        addStyleName(Runo.TABSHEET_SMALL);

        Panel legalPanel;
        if (StringUtils.hasLength(termsAndConditions.getText())) {
            legalPanel = new Panel();
            legalPanel.setSizeFull();
            legalPanel.addStyleName(Runo.PANEL_LIGHT);
            legalPanel.setContent(new Label(termsAndConditions.getText(), ContentMode.HTML));
            addTab(legalPanel).setCaption(termsAndConditions.getLabel());
            ++tabCount;
        }

        if (StringUtils.hasLength(privacyPolicy.getText())) {
            legalPanel = new Panel();
            legalPanel.setSizeFull();
            legalPanel.addStyleName(Runo.PANEL_LIGHT);
            legalPanel.setContent(new Label(privacyPolicy.getText(), ContentMode.HTML));
            addTab(legalPanel).setCaption(privacyPolicy.getLabel());
            ++tabCount;
        }

        if (StringUtils.hasLength(cookiesPolicy.getText())) {
            legalPanel = new Panel();
            legalPanel.setSizeFull();
            legalPanel.addStyleName(Runo.PANEL_LIGHT);
            legalPanel.setContent(new Label(cookiesPolicy.getText(), ContentMode.HTML));
            addTab(legalPanel).setCaption(cookiesPolicy.getLabel());
            ++tabCount;
        }

        if (tabCount == 0) {
            legalPanel = new Panel();
            legalPanel.setSizeFull();
            legalPanel.addStyleName(Runo.PANEL_LIGHT);
            legalPanel.setContent(new Label("No policies exist for terms of service, privacy or cookies.", ContentMode.TEXT));
            addTab(legalPanel).setCaption("Policies");
        }
    }


    public int getTabCount() {
        return tabCount;
    }
}
