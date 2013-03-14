package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * Date: 3/5/13
 * Time: 11:23 PM
 */
public class TitleBarComposite extends CustomComponent {

    private HorizontalLayout mainLayout;

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public TitleBarComposite(final AppUser appUser) {
        buildMainLayout(appUser);
        setCompositionRoot(mainLayout);
    }

    private void buildMainLayout(final AppUser appUser) {
        setWidth(100, Unit.PERCENTAGE);
        setHeight(null);
        // the main layout and components will be created here
        mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        Label welcomeLabel = new Label("Welcome " + appUser.getViewableDescription());
        welcomeLabel.setWidth(null);
        mainLayout.addComponent(welcomeLabel);
        mainLayout.setComponentAlignment(welcomeLabel, Alignment.MIDDLE_LEFT);
        mainLayout.setExpandRatio(welcomeLabel, 0.2f);

        Label appLabel = new Label("E-EYE-O");
        appLabel.setWidth(null);
        mainLayout.addComponent(appLabel);
        mainLayout.setComponentAlignment(appLabel, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(appLabel, 0.5f);
    }

}