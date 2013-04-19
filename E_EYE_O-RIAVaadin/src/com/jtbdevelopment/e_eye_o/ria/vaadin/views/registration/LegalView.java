package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
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
    public final static String VIEW_NAME = "LegalStuff";
    public static final String AGREEMENT_TIME = "agreementTime";

    @Autowired
    private Logo logo;

    @PostConstruct
    public void setup() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        addComponent(logo);
        setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        Panel legalPanel = new Panel();
        legalPanel.setWidth(90, Unit.PERCENTAGE);
        legalPanel.addStyleName(Runo.PANEL_LIGHT);
        VerticalLayout legalLayout = new VerticalLayout();
        legalLayout.setSizeFull();
        legalPanel.setContent(legalLayout);

        legalLayout.addComponent(new Label("Terms of Agreement and Service"));
        legalLayout.addComponent(new Label("blah blah blah we promise to be good but make no promises.."));
        legalLayout.addComponent(new Label("..don't sue me bro."));
        addComponent(legalPanel);
        setComponentAlignment(legalPanel, Alignment.MIDDLE_CENTER);
        setExpandRatio(legalPanel, 1);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setMargin(true);
        horizontalLayout.setSpacing(true);

        Button acceptButton = new Button("I Agree.");
        acceptButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                DateTime agreementTime = new DateTime();
                //  TODO - store this somewhere
                getSession().setAttribute(AGREEMENT_TIME, agreementTime);
                getSession().getAttribute(Navigator.class).navigateTo(RegistrationView.VIEW_NAME);
            }
        });
        horizontalLayout.addComponent(acceptButton);
        Button disagreeButton = new Button("I Do Not Agree.");
        disagreeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                Notification.show("Well this is awkward then..", Notification.Type.WARNING_MESSAGE);
                getSession().setAttribute(AGREEMENT_TIME, null);
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
