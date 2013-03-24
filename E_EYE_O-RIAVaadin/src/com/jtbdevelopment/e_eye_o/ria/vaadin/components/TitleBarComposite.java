package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

    public TitleBarComposite() {
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

        Label appLabel = new Label("E-EYE-O");
        appLabel.setWidth(null);
        mainLayout.addComponent(appLabel);
        mainLayout.setComponentAlignment(appLabel, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(appLabel, 0.5f);
        setCompositionRoot(mainLayout);
    }

    public void setAppUser(final AppUser appUser) {
        welcomeLabel.setValue("Welcome " + appUser.getViewableDescription());
    }
}
