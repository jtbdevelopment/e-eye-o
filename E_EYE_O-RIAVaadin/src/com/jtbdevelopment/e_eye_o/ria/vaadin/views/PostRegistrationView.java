package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
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
    private Label action;
    private Label dummy;


    @PostConstruct
    public void setUp() {
        setSizeFull();
        setMargin(true);
        setSpacing(true);

        //  TODO - actually send email
        action = new Label("An email will be sent");

        dummy = new Label("");


        addComponent(action);
        addComponent(dummy);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        TwoPhaseActivity twoPhaseActivity = getSession().getAttribute(TwoPhaseActivity.class);
        final AppUser appUser = twoPhaseActivity.getAppUser();
        action.setValue("Welcome aboard " + appUser.getFirstName() + "!  An email has been sent to " + appUser.getEmailAddress() + " to verify it.  Please follow the instructions to activate your account.");
        dummy.setValue(twoPhaseActivity.getSummaryDescription());
        Link link = new Link("Click", new ExternalResource("#!" + AccountConfirmationView.VIEW_NAME + "/" + twoPhaseActivity.getId()));
        addComponent(link);
    }
}
