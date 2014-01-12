package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.DeletionHelper;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserMaintenanceHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private DeletionHelper deletionHelper;

    @Autowired
    private ChangeEmailAddressEmailGenerator changeEmailAddressEmailGenerator;

    @Autowired
    private DeletedAccountEmailGenerator deletedAccountEmailGenerator;

    @Autowired
    private DeactivateAccountEmailGenerator deactivateAccountEmailGenerator;

    @Autowired
    private ChangePasswordEmailGenerator confirmPasswordEmailGenerator;

    @Autowired
    private UserMaintenanceHelper userMaintenanceHelper;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private EventBus eventBus;

    private TextField firstName;
    private TextField lastName;
    private Label email;
    private Button changeEmail;
    private Button changePassword;
    private Label warning;

    @PostConstruct
    public void postConstruct() {
        Panel panel = new Panel();
        panel.addStyleName(Runo.PANEL_LIGHT);
        setCompositionRoot(panel);
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        panel.setContent(verticalLayout);

        verticalLayout.addComponent(new Label("For security you cannot change your password and your email at the same time.  If you are experiencing a security breach, change your password first."));

        GridLayout grid = new GridLayout(2, 8);
        grid.setMargin(true);
        grid.setSpacing(true);
        grid.setSizeUndefined();
        verticalLayout.addComponent(grid);

        firstName = new TextField();
        buildRow(grid, "First Name:", firstName);

        lastName = new TextField();
        buildRow(grid, "Last Name:", lastName);

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
        Button reset = new Button("Reset");
        reset.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                initForUser();
            }
        });
        buildRow(grid, null, save, reset);
        buildRow(grid, null, new Label(), new Label());

        email = new Label();
        buildRow(grid, "Email:", email);

        changeEmail = new Button("Change Email");
        changeEmail.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final AppUser appUser = getUI().getSession().getAttribute(AppUser.class);
                ConfirmEmailChange confirmEmailChange = new ConfirmEmailChange(appUser, userMaintenanceHelper, authenticationManager, changeEmailAddressEmailGenerator);
                getUI().addWindow(confirmEmailChange);
                initForUser();
            }
        });

        changePassword = new Button("Change Password");
        changePassword.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final AppUser appUser = changePassword.getUI().getSession().getAttribute(AppUser.class);
                ConfirmPasswordChange confirmPasswordChange = new ConfirmPasswordChange(appUser, userMaintenanceHelper, authenticationManager, confirmPasswordEmailGenerator);
                getUI().addWindow(confirmPasswordChange);
            }
        });
        buildRow(grid, null, changeEmail, changePassword);
        buildRow(grid, null, new Label(), new Label());

        Button deactivate = new Button("Deactivate Account");
        deactivate.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                ConfirmDialog.show(firstName.getUI(), "Deactivating your account will not immediately delete data and you can reactivate your account by using password reset on login screen.  Continue?", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(final ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            AppUser user = readWriteDAO.get(AppUser.class, getSession().getAttribute(AppUser.class).getId());
                            deletionHelper.deactivateUser(user);
                            LogoutEvent logoutEvent = new LogoutEvent(user);
                            deactivateAccountEmailGenerator.generateAccountDeactivatedEmail(user);
                            eventBus.post(logoutEvent);
                        }
                    }
                });
            }
        });
        Button delete = new Button("Delete Account");
        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                AppUser user = readWriteDAO.get(AppUser.class, getSession().getAttribute(AppUser.class).getId());
                ConfirmDeleteAccount deleteAccount = new ConfirmDeleteAccount(user, deletionHelper, authenticationManager, deletedAccountEmailGenerator);
                getUI().addWindow(deleteAccount);
            }
        });
        buildRow(grid, null, deactivate, delete);

        //  TODO - configurable
        warning = new Label("Password and email cannot be changed within one week of each other.");
        verticalLayout.addComponent(warning);
        ComponentUtils.setImmediateForAll(this, true);
    }

    private void buildRow(final GridLayout layout, final String content, final com.vaadin.ui.Component... components) {
        if (StringUtils.hasLength(content)) {
            layout.addComponent(new Label(content));
        }
        for (com.vaadin.ui.Component component : components) {
            layout.addComponent(component);
            layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
        }
    }

    @Override
    public void detach() {
        eventBus.unregister(this);
    }

    @Override
    public void attach() {
        super.attach();
        initForUser();
        eventBus.register(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handleUpdate(final IdObjectChanged idObjectChanged) {
        if (AppUser.class.isAssignableFrom(idObjectChanged.getEntityType())) {
            if (idObjectChanged.getChangeType().equals(IdObjectChanged.ChangeType.UPDATED)) {
                if (idObjectChanged.getEntity().equals(getSession().getAttribute(AppUser.class))) {
                    getSession().setAttribute(AppUser.class, (AppUser) idObjectChanged.getEntity());
                    initForUser();
                }
            }
        }
    }

    private void initForUser() {
        AppUser user = getSession().getAttribute(AppUser.class);
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        email.setValue(user.getEmailAddress());
        warning.setVisible(false);
        if (userMaintenanceHelper.canChangeEmailAddress(user)) {
            changeEmail.setEnabled(true);
        } else {
            changeEmail.setEnabled(false);
            warning.setVisible(true);
        }

        if (userMaintenanceHelper.canChangePassword(user)) {
            changePassword.setEnabled(true);
        } else {
            changePassword.setEnabled(false);
            warning.setVisible(true);
        }
    }
}
