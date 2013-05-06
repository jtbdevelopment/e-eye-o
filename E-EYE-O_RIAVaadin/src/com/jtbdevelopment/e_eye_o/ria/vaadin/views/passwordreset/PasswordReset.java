package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Date: 4/7/13
 * Time: 8:29 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PasswordReset extends VerticalLayout implements View {
    public static final String VIEW_NAME = "PasswordReset";

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private UserHelper userHelper;
    private TextField emailField;
    private FormLayout formLayout;
    private Button resetButton;
    private Button newRequest;

    @PostConstruct
    public void setUp() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        Label title = new Label("Reset Password");
        addComponent(title);
        setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        formLayout = new FormLayout();
        emailField = new TextField("Email Address");
        emailField.setReadOnly(true);
        formLayout.addComponent(emailField);
        final PasswordField password = new PasswordField("New Password");
        final PasswordField confirm = new PasswordField("Confirm Password");
        formLayout.addComponent(password);
        formLayout.addComponent(confirm);
        addComponent(formLayout);
        setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

        resetButton = new Button("Reset");
        addComponent(resetButton);
        setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);

        resetButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String pass = password.getValue();
                if (!StringUtils.hasLength(pass)) {
                    Notification.show("Password cannot be blank.");
                    return;
                }
                if (!pass.equals(confirm.getValue())) {
                    Notification.show("Passwords do not match.");
                    return;
                }

                userHelper.resetPassword(getSession().getAttribute(TwoPhaseActivity.class), pass);
                getSession().getAttribute(Navigator.class).navigateTo(LoginView.VIEW_NAME);
                Notification.show("Password reset and account activated.");
            }
        });
        newRequest = new Button("New Email");
        newRequest.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                //  TODO - new email/activity
            }
        });
        newRequest.setVisible(false);
        newRequest.setEnabled(false);
        ComponentUtils.setImmediateForAll(this, true);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        String id = event.getParameters();
        formLayout.setVisible(true);
        formLayout.setEnabled(true);
        resetButton.setVisible(true);
        resetButton.setEnabled(true);
        newRequest.setEnabled(false);
        newRequest.setVisible(true);

        if (!StringUtils.hasLength(id)) {
            Notification.show("This is embarrassing.  This link is wrong.");
            getSession().getAttribute(Navigator.class).navigateTo(LoginView.VIEW_NAME);
            return;
        }

        TwoPhaseActivity activity = readOnlyDAO.get(TwoPhaseActivity.class, id);
        if (!TwoPhaseActivity.Activity.PASSWORD_RESET.equals(activity.getActivityType())) {
            Notification.show("This is embarrassing.  This link is wrong type.");
            getSession().getAttribute(Navigator.class).navigateTo(LoginView.VIEW_NAME);
            return;
        }

        if (new DateTime().compareTo(activity.getExpirationTime()) > 0) {
            Notification.show("This request has expired.  Press the button to generate another.");
            formLayout.setEnabled(false);
            formLayout.setVisible(false);
            resetButton.setEnabled(false);
            resetButton.setVisible(false);
            newRequest.setVisible(true);
            newRequest.setEnabled(true);
            return;
        }

        emailField.setReadOnly(false);
        emailField.setValue(activity.getAppUser().getEmailAddress());
        emailField.setReadOnly(true);
    }
}
