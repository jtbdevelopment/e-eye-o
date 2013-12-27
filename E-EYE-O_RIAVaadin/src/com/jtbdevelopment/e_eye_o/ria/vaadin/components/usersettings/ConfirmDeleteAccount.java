package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Date: 9/26/13
 * Time: 10:24 PM
 */
public class ConfirmDeleteAccount extends PasswordConfirmingWindow {
    private final PasswordField passwordField = new PasswordField();

    public ConfirmDeleteAccount(final AppUser appUser, final DeletionHelper deletionHelper, final AuthenticationManager authenticationManager, final DeletedAccountEmailGenerator deletedAccountEmailGenerator) {
        super("Confirm delete account");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setSizeFull();

        setWidth(30, Unit.EM);
        setHeight(10, Unit.EM);


        Layout row = new GridLayout(2, 2);
        row.addComponent(new Label("Enter Password:"));
        row.addComponent(passwordField);
        mainLayout.addComponent(row);
        mainLayout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row = new HorizontalLayout();
        Button ok = new Button("Change Password");
        ok.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (!reconfirmPassword(authenticationManager, appUser, passwordField.getValue())) return;
                final String email = appUser.getEmailAddress();
                deletionHelper.deleteUser(appUser);
                deletedAccountEmailGenerator.generateAccountDeletedEmail(email);
                getUI().removeWindow(ConfirmDeleteAccount.this);
                Notification.show("Password changed.");
            }
        });
        row.addComponent(ok);
        Button cancel = new Button("Cancel");
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                getUI().removeWindow(ConfirmDeleteAccount.this);
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
