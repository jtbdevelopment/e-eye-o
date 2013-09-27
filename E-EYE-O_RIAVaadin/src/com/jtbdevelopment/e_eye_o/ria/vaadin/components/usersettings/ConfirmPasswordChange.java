package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Date: 9/26/13
 * Time: 10:24 PM
 */
public class ConfirmPasswordChange extends PasswordConfirmingWindow {
    private final PasswordField existingPassword = new PasswordField();
    private final PasswordField newPassword1 = new PasswordField();
    private final PasswordField newPassword2 = new PasswordField();

    public ConfirmPasswordChange(final AppUser appUser, final UserHelper userHelper, final AuthenticationManager authenticationManager, final ChangePasswordEmailGenerator confirmPasswordChangeoldAddress) {
        super("Confirm password change");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSizeFull();

        setWidth(30, Sizeable.Unit.EM);
        setHeight(14, Sizeable.Unit.EM);


        Layout row = new GridLayout(2, 3);
        row.addComponent(new Label("Existing Password:"));
        row.addComponent(existingPassword);
        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row.addComponent(new Label("New Password:"));
        row.addComponent(newPassword1);
        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row.addComponent(new Label("Confirm Password:"));
        row.addComponent(newPassword2);
        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row = new HorizontalLayout();
        Button ok = new Button("Change Password");
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!newPassword1.getValue().equals(newPassword2.getValue())) {
                    Notification.show("New passwords do not match.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                if (reconfirmPassword(authenticationManager, appUser, existingPassword.getValue())) return;
                try {
                    TwoPhaseActivity activity = userHelper.requestResetPassword(appUser);
                    userHelper.resetPassword(activity, newPassword1.getValue());
                    confirmPasswordChangeoldAddress.generatePasswordChangeEmail(appUser);
                } catch (UserHelper.EmailChangeTooRecent passwordChangeTooRecent) {
                    Notification.show("Email was changed too recently to also change password.");
                }
                getUI().removeWindow(ConfirmPasswordChange.this);
                Notification.show("Password changed.");
            }
        });
        row.addComponent(ok);
        Button cancel = new Button("Cancel");
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().removeWindow(ConfirmPasswordChange.this);
            }
        });
        row.addComponent(cancel);
        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);
        addStyleName(Runo.WINDOW_DIALOG);
        setModal(true);

        setContent(mainLayout);
    }
}
