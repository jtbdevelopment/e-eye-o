package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.legal.LegalTabSheet;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 4/6/13
 * Time: 6:33 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LegalView extends VerticalLayout implements View {
    public final static String VIEW_NAME = "Legal";

    @Autowired
    private Logo logo;

    @Autowired
    private LegalTabSheet legalTabSheet;

    @PostConstruct
    public void setup() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(logo);
        setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        addComponent(legalTabSheet);
        setComponentAlignment(legalTabSheet, Alignment.MIDDLE_CENTER);
        setExpandRatio(legalTabSheet, 1);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMargin(true);
        horizontalLayout.setSpacing(true);

        Button acceptButton = new Button("I Agree.");
        acceptButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                DateTime agreementTime = new DateTime();
                getSession().getAttribute(Navigator.class).navigateTo(RegistrationView.VIEW_NAME);
            }
        });
        horizontalLayout.addComponent(acceptButton);
        Button disagreeButton = new Button("I Do Not Agree.");
        disagreeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                Notification.show("Well this is awkward then..", Notification.Type.WARNING_MESSAGE);
                getSession().getAttribute(Navigator.class).navigateTo(LoginView.VIEW_NAME);
            }
        });
        horizontalLayout.addComponent(disagreeButton);

        addComponent(horizontalLayout);
        setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
    }
}

