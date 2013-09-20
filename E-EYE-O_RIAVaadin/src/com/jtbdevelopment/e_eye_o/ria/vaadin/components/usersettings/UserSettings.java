package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;


import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset.PasswordResetEmailGenerator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.dialogs.ConfirmDialog;

import javax.annotation.PostConstruct;

/**
 * Date: 6/11/13
 * Time: 6:42 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserSettings extends CustomComponent {

    @Autowired
    private PasswordResetEmailGenerator passwordResetEmailGenerator;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private EventBus eventBus;

    private TextField firstName;
    private TextField lastName;
    private TextField email;

    @PostConstruct
    public void postConstruct() {
        Panel panel = new Panel();
        panel.addStyleName(Runo.PANEL_LIGHT);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.setSizeUndefined();
        panel.setContent(layout);
        setCompositionRoot(panel);
        setSizeFull();

        FormLayout formLayout = new FormLayout();
        formLayout.setMargin(true);
        layout.addComponent(formLayout);

        firstName = new TextField("First Name:");
        formLayout.addComponent(firstName);
        lastName = new TextField("Last Name:");
        formLayout.addComponent(lastName);
        email = new TextField("Email:");
        email.setReadOnly(true);
        formLayout.addComponent(email);

        HorizontalLayout row = new HorizontalLayout();
        row.setSizeUndefined();
        Button save = new Button("Save");
        save.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                AppUser user = readWriteDAO.get(AppUser.class, getSession().getAttribute(AppUser.class).getId());
                user.setFirstName(firstName.getValue());
                user.setLastName(lastName.getValue());
                readWriteDAO.update(user, user);
                Notification.show("Changes saved.", Notification.Type.HUMANIZED_MESSAGE);
            }
        });
        row.addComponent(save);
        Button reset = new Button("Reset");
        reset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                initForUser();
            }
        });
        row.addComponent(reset);
        layout.addComponent(row);
        layout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row = new HorizontalLayout();
        row.setSizeUndefined();
        final Button changeEmail = new Button("Change Email");
        changeEmail.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show("Change email not implemented yet.");
                //  TODO
            }
        });
        row.addComponent(changeEmail);
        final Button changePassword = new Button("Change Password");
        row.addComponent(changePassword);
        changePassword.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                AppUser appUser = changePassword.getUI().getSession().getAttribute(AppUser.class);
                final TwoPhaseActivity twoPhaseActivity = userHelper.requestResetPassword(appUser);
                passwordResetEmailGenerator.generateEmail(twoPhaseActivity);
                ConfirmDialog.show(changePassword.getUI(),
                        "Password change requested.",
                        "A password reset request has been sent to "
                                + appUser.getEmailAddress()
                                + " from "
                                + passwordResetEmailGenerator.getResetEmailFrom() + ".  Follow the directions within it.",
                        "OK", "Cancel Request",
                        new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (!dialog.isConfirmed()) {
                                    twoPhaseActivity.setExpirationTime(new DateTime());
                                    readWriteDAO.cancelResetPassword(twoPhaseActivity);
                                    Notification.show("Request cancelled.  Ignore email.", Notification.Type.ERROR_MESSAGE);
                                }
                            }
                        });
            }
        });
        layout.addComponent(row);
        layout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

        row = new HorizontalLayout();
        row.setSizeUndefined();
        Button deactivate = new Button("Deactivate Account");
        deactivate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.show(firstName.getUI(), "Deactivating your account will not immediately delete data and you can reactivate your account by using password reset on login screen.  Continue?", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            AppUser user = readWriteDAO.get(AppUser.class, getSession().getAttribute(AppUser.class).getId());
                            readWriteDAO.deactivateUser(user);
                            LogoutEvent logoutEvent = new LogoutEvent(user);
                            eventBus.post(logoutEvent);
                        }
                    }
                });
            }
        });

        row.addComponent(deactivate);
        Button delete = new Button("Delete Account");
        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                ConfirmDialog confirmDialog = new ConfirmDialog();
                confirmDialog.show(firstName.getUI(), "Deleting your account is permanent and all your data is deleted immediately.  You can re-register but you will be starting with a blank new account.  Continue?", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            AppUser user = readWriteDAO.get(AppUser.class, getSession().getAttribute(AppUser.class).getId());
                            readWriteDAO.deleteUser(user);
                        }
                    }
                });
            }
        });
        row.addComponent(delete);
        layout.addComponent(row);
        layout.setComponentAlignment(row, Alignment.MIDDLE_CENTER);

    }

    @Override
    public void attach() {
        super.attach();
        initForUser();
    }

    private void initForUser() {
        AppUser user = getSession().getAttribute(AppUser.class);
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        email.setReadOnly(false);
        email.setValue(user.getEmailAddress());
        email.setReadOnly(true);
    }
}
