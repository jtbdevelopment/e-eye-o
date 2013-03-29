package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/5/13
 * Time: 11:23 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TitleBarComposite extends CustomComponent {

    private final Label welcomeLabel;
    private final Label lastLogout;

    @Autowired
    public TitleBarComposite(final Logo logo) {
        setWidth(100, Unit.PERCENTAGE);
        setHeight(null);
        // the main layout and components will be created here
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        welcomeLabel = new Label("Welcome");
        welcomeLabel.setWidth(null);
        mainLayout.addComponent(welcomeLabel);
        mainLayout.setComponentAlignment(welcomeLabel, Alignment.MIDDLE_LEFT);
        mainLayout.setExpandRatio(welcomeLabel, 0.2f);

        mainLayout.addComponent(logo);
        mainLayout.addComponent(logo);
        mainLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(logo, 0.75f);

        lastLogout = new Label();
        lastLogout.setWidth(null);
        mainLayout.addComponent(lastLogout);
        mainLayout.addStyleName("titlebar");
        setCompositionRoot(mainLayout);
    }

    @Override
    public void attach() {
        super.attach();
        AppUser appUser = getUI().getSession().getAttribute(AppUser.class);
        if (appUser.getLastLogout().equals(AppUser.NEVER_LOGGED_IN)) {
            welcomeLabel.setValue("Welcome " + appUser.getSummaryDescription());
        } else {
            welcomeLabel.setValue("Welcome back " + appUser.getSummaryDescription());
        }
        lastLogout.setValue("Last Session: " + appUser.getLastLogout().toString("YYYY-MM-dd HH:mm", getUI().getLocale()));
    }
}
