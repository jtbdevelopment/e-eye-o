package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 4/6/13
 * Time: 7:36 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PostRegistrationView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PostRegistration";
    private final Label action = new Label("", ContentMode.HTML);

    @Autowired
    private RegistrationEmailGenerator registrationEmailGenerator;

    @Autowired
    private Logo logo;

    @PostConstruct
    public void setUp() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();
        verticalLayout.addComponent(logo);
        verticalLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        verticalLayout.addComponent(action);
        verticalLayout.setComponentAlignment(action, Alignment.MIDDLE_CENTER);

        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        TwoPhaseActivity twoPhaseActivity = getSession().getAttribute(TwoPhaseActivity.class);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        //  TODO - template too?
        action.setValue("<CENTER>Welcome aboard " + appUser.getFirstName() + "!</CENTER>" +
                "<P><CENTER>An email has been sent to " + appUser.getEmailAddress() + " to verify it.</CENTER>" +
                "<P><CENTER>It will be sent from " + registrationEmailGenerator.getRegistrationEmailFrom() + ".  Please check spam folders if you do not receive it.</CENTER>" +
                "<P><CENTER>Please follow the instructions to activate your account.</CENTER>");
    }
}
