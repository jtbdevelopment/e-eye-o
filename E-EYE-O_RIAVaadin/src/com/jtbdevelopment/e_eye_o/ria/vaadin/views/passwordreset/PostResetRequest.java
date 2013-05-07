package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

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
 * Date: 4/7/13
 * Time: 8:15 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PostResetRequest extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PostReset";

    @Autowired
    private Logo logo;

    @Autowired
    private PasswordResetEmailGenerator passwordResetEmailGenerator;

    private Label info = new Label("", ContentMode.HTML);

    @PostConstruct
    public void setUp() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();

        verticalLayout.addComponent(logo);
        verticalLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        verticalLayout.addComponent(info);
        verticalLayout.setComponentAlignment(info, Alignment.MIDDLE_CENTER);

        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        TwoPhaseActivity activity = getSession().getAttribute(TwoPhaseActivity.class);
        info.setValue("<center><p>An email confirming the reset has been sent to " + activity.getAppUser().getEmailAddress() + " from " + passwordResetEmailGenerator.getResetEmailFrom() +
                "<p>Please check spam folders if you do not receive it.</center>");
    }
}
