package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.security.authentication.AuthenticationManager;


/**
 * Date: 9/24/13
 * Time: 8:02 PM
 */

public class ConfirmEmailChange extends PasswordConfirmingWindow {
    private final TextField email = new TextField();
    private final PasswordField passwordField = new PasswordField();

    public ConfirmEmailChange(final AppUser appUser, final UserHelper userHelper, final AuthenticationManager authenticationManager, final ChangeEmailAddressEmailGenerator changeEmailAddressEmailGenerator) {
        super("Confirm email address change");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSizeFull();

        setWidth(30, Unit.EM);
        setHeight(12, Unit.EM);


        Layout row = new GridLayout(2, 2);
        row.addComponent(new Label("New Address:"));
        row.addComponent(email);
        email.setValue(appUser.getEmailAddress());
        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row.addComponent(new Label("Re-enter password:"));
        row.addComponent(passwordField);

        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row = new HorizontalLayout();
        Button ok = new Button("Change Email");
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!reconfirmPassword(authenticationManager, appUser, passwordField.getValue())) return;
                try {
                    String oldAddress = appUser.getEmailAddress();
                    userHelper.changeEmailAddress(appUser, email.getValue());
                    changeEmailAddressEmailGenerator.generateEmailAddressChangeEmail(oldAddress, email.getValue());
                } catch (UserHelper.PasswordChangeTooRecent passwordChangeTooRecent) {
                    Notification.show("Password was changed too recently to also change email address.");
                }
                getUI().removeWindow(ConfirmEmailChange.this);
                Notification.show("Email changed.  You will need to relogin the next time you use this service if you clicked 'Remember Me'.");
            }
        });
        row.addComponent(ok);
        Button cancel = new Button("Cancel");
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().removeWindow(ConfirmEmailChange.this);
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
