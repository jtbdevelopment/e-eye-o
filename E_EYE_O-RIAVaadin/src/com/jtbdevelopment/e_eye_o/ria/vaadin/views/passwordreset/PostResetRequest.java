package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

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
 * Date: 4/7/13
 * Time: 8:15 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PostResetRequest extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PostReset";

    @PostConstruct
    public void setUp() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        TwoPhaseActivity activity = getSession().getAttribute(TwoPhaseActivity.class);
        addComponent(new Label("An email confirming the reset has been set to " + activity.getAppUser().getEmailAddress()));
        Link link = new Link("Click", new ExternalResource("#!" + PasswordReset.VIEW_NAME + "/" + activity.getId()));
        addComponent(link);
    }
}
