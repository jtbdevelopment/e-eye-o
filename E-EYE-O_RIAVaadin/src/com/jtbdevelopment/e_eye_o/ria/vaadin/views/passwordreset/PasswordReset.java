package com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserMaintenanceHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
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
    private Logo logo;

    @Autowired
    private PasswordResetEmailGenerator passwordResetEmailGenerator;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private UserMaintenanceHelper userMaintenanceHelper;
    private TextField emailField;
    private FormLayout formLayout;
    private Button resetButton;
    private Button newRequest;
    private AppUser appUserForRequest;

    @PostConstruct
    public void setUp() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();

        verticalLayout.addComponent(logo);
        verticalLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        Label title = new Label("<H2>Reset Password</H2>", ContentMode.HTML);
        title.setSizeUndefined();
        verticalLayout.addComponent(title);
        verticalLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        formLayout = new FormLayout();
        formLayout.setSizeUndefined();
        emailField = new TextField("Email Address");
        emailField.setReadOnly(true);
        emailField.setWidth(1, Unit.PERCENTAGE);
        formLayout.addComponent(emailField);
        final PasswordField password = new PasswordField("New Password");
        final PasswordField confirm = new PasswordField("Confirm Password");
        formLayout.addComponent(password);
        formLayout.addComponent(confirm);
        verticalLayout.addComponent(formLayout);
        verticalLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

        resetButton = new Button("Reset");
        verticalLayout.addComponent(resetButton);
        verticalLayout.setComponentAlignment(resetButton, Alignment.MIDDLE_CENTER);

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

                userMaintenanceHelper.resetPassword(getSession().getAttribute(TwoPhaseActivity.class), pass);
                getSession().getAttribute(Navigator.class).navigateTo(LoginView.VIEW_NAME);
                Notification.show("Password reset and account activated.", Notification.Type.WARNING_MESSAGE);
            }
        });
        newRequest = new Button("New Email");
        newRequest.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (appUserForRequest != null) {
                    TwoPhaseActivity twoPhaseActivity;
                    try {
                        twoPhaseActivity = userMaintenanceHelper.requestResetPassword(appUserForRequest);
                    } catch (UserMaintenanceHelper.EmailChangeTooRecent e) {
                        Notification.show("Email was changed too recently to also change password.", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    appUserForRequest = null;
                    passwordResetEmailGenerator.generatePasswordResetEmail(twoPhaseActivity);
                    getSession().setAttribute(TwoPhaseActivity.class, twoPhaseActivity);
                    getSession().getAttribute(Navigator.class).navigateTo(PostResetRequest.VIEW_NAME);
                }
                newRequest.setEnabled(false);
            }
        });
        newRequest.setVisible(false);
        newRequest.setEnabled(false);

        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        ComponentUtils.setImmediateForAll(this, true);
        ComponentUtils.setTextFieldWidths(this, 20, Unit.EM);
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
