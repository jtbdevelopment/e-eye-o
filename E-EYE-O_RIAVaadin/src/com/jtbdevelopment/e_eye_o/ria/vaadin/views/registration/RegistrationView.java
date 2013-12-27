package com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserCreationHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 4/6/13
 * Time: 6:32 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegistrationView extends VerticalLayout implements View {
    public final static String VIEW_NAME = "Registration";

    @Autowired
    private Logo logo;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @Autowired
    private UserCreationHelper userCreationHelper;

    @Autowired
    private ReadWriteDAO readOnlyDAO;

    @Autowired
    private RegistrationEmailGenerator registrationEmailGenerator;

    @PostConstruct
    public void setUp() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(logo);
        verticalLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        Label formTitle = new Label("<H2>Account Details</H2>", ContentMode.HTML);
        formTitle.setSizeUndefined();
        verticalLayout.addComponent(formTitle);
        verticalLayout.setComponentAlignment(formTitle, Alignment.MIDDLE_CENTER);

        FormLayout form = new FormLayout();
        final BeanFieldGroup<AppUser> beanFieldGroup = new BeanFieldGroup<>(AppUser.class);
        beanFieldGroup.setItemDataSource(idObjectFactory.newAppUser());

        final Field<?> emailAddress = beanFieldGroup.buildAndBind("emailAddress");
        form.addComponent(emailAddress);
        form.setComponentAlignment(emailAddress, Alignment.MIDDLE_CENTER);
        final Field<?> firstName = beanFieldGroup.buildAndBind("firstName");
        form.addComponent(firstName);
        form.setComponentAlignment(firstName, Alignment.MIDDLE_CENTER);
        final Field<?> lastName = beanFieldGroup.buildAndBind("lastName");
        form.addComponent(lastName);
        form.setComponentAlignment(lastName, Alignment.MIDDLE_CENTER);
        final PasswordField passwordField = new PasswordField("Password");
        form.addComponent(passwordField);
        form.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);
        beanFieldGroup.bind(passwordField, "password");
        final PasswordField confirmPassword = new PasswordField("Confirm Password");
        form.addComponent(confirmPassword);
        form.setComponentAlignment(confirmPassword, Alignment.MIDDLE_CENTER);
        form.setSizeUndefined();

        verticalLayout.addComponent(form);
        verticalLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        Button register = new Button("Register");
        verticalLayout.addComponent(register);
        verticalLayout.setComponentAlignment(register, Alignment.MIDDLE_CENTER);
        register.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if (!passwordField.getValue().equals(confirmPassword.getValue())) {
                    Notification.show("Passwords don't match.  Please try again.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                AppUser appUser = readOnlyDAO.getUser(emailAddress.getValue().toString());
                if (appUser != null) {
                    Notification.show("We're sorry.  This email address is already registered.  Please try again.  Or use the reset password feature if necessary.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                try {
                    beanFieldGroup.commit();
                } catch (FieldGroup.CommitException e) {
                    e.printStackTrace();
                }

                AppUser user = beanFieldGroup.getItemDataSource().getBean();
                TwoPhaseActivity twoPhaseActivity = userCreationHelper.createNewUser(user);
                registrationEmailGenerator.generateEmail(twoPhaseActivity);
                getSession().setAttribute(TwoPhaseActivity.class, twoPhaseActivity);
                getSession().getAttribute(Navigator.class).navigateTo(PostRegistrationView.VIEW_NAME);
            }
        });
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        ComponentUtils.setImmediateForAll(this, true);
        ComponentUtils.setTextFieldWidths(this, 20, Unit.EM);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
