package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.helpandlegal.Help;
import com.jtbdevelopment.e_eye_o.helpandlegal.SafetyTips;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.legal.LegalTabSheet;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.quicktour.QuickTour;
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
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Date: 5/2/13
 * Time: 9:05 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HelpWorkArea extends CustomComponent {
    @Autowired
    private LegalTabSheet legalTabSheet;

    @Autowired
    private SafetyTips safetyTips;

    @Autowired
    private Help help;

    @Autowired
    private QuickTour quickTour;

    @PostConstruct
    public void postConstruct() {
        setSizeFull();

        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(Runo.TABSHEET_SMALL);
        tabSheet.setSizeFull();

        Panel panel;

        if (StringUtils.hasLength(help.getText())) {
            panel = new Panel();
            panel.setSizeFull();
            panel.addStyleName(Runo.PANEL_LIGHT);
            panel.setContent(new Label(help.getText(), ContentMode.HTML));
            tabSheet.addTab(panel).setCaption(help.getLabel());
        }

        panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName(Runo.PANEL_LIGHT);
        panel.setContent(quickTour);
        tabSheet.addTab(panel).setCaption("Quick Tour");

        if (StringUtils.hasLength(safetyTips.getText())) {
            panel = new Panel();
            panel.setSizeFull();
            panel.addStyleName(Runo.PANEL_LIGHT);
            panel.setContent(new Label(safetyTips.getText(), ContentMode.HTML));
            tabSheet.addTab(panel).setCaption(safetyTips.getLabel());
        }
        if (legalTabSheet.getTabCount() > 0) {
            tabSheet.addTab(legalTabSheet).setCaption("Legal");
        }

        setCompositionRoot(tabSheet);
    }
}
