package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings.UserPreferences;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings.UserSettings;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 5/7/13
 * Time: 11:26 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SettingsWorkArea extends CustomComponent {
    @Autowired
    private UserPreferences userPreferences;

    @Autowired
    private UserSettings userSettings;

    @PostConstruct
    public void postConstruct() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        TabSheet tabSheet = new TabSheet();
        tabSheet.addStyleName(Runo.TABSHEET_SMALL);
        tabSheet.addTab(userPreferences, "Preferences");
        tabSheet.addTab(userSettings, "Personal");
        tabSheet.setSizeFull();

        mainLayout.addComponent(tabSheet);
        setCompositionRoot(mainLayout);
    }
}
