package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "Login";

    private final TextField loginField = new TextField("Email Address");
    private final PasswordField passwordField = new PasswordField("Password");

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @PostConstruct
    public void PostConstruct() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();


        VerticalLayout verticalLayout = new VerticalLayout();
        addComponent(verticalLayout);
        setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        Label title = new Label("Welcome to E-EYE-O");
        title.setWidth(null);
        title.setHeight(null);
        title.addStyleName("bold");
        verticalLayout.addComponent(title);
        verticalLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);

        FormLayout form = new FormLayout();
        form.setWidth(null);
        form.setHeight(null);
        verticalLayout.addComponent(form);
        verticalLayout.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        form.addComponent(loginField);
        form.setComponentAlignment(loginField, Alignment.TOP_CENTER);
        form.addComponent(passwordField);
        form.setComponentAlignment(passwordField, Alignment.TOP_CENTER);

        Button loginButton = new Button("Login");
        form.addComponent(loginButton);
        form.setComponentAlignment(loginButton, Alignment.TOP_CENTER);
        loginButton.addStyleName("primary");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Authentication authentication;
                String login = loginField.getValue();
                final String password = passwordField.getValue();
                try {
                    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
                } catch (AuthenticationException e) {
                    Notification.show("Failed to login.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                //  TODO
                login = login + "@test.com";
                AppUser user = readOnlyDAO.getUser(login);
                if (user == null) {
                    readWriteDAO.create(idObjectFactory.newAppUserBuilder().withLastName(login).withFirstName(login).withEmailAddress(login).build());
                    Notification.show("This is embarrassing", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                getParent().getUI().getSession().close();
                getParent().getUI().getPage().setLocation("/E-EYE-O");
                loginField.setValue("");
                passwordField.setValue("");
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
        Link registerLink = new Link("Register for Account", new ExternalResource("#!" + ToDoView.VIEW_NAME));
        horizontalLayout.addComponent(registerLink);
        Link forgotPasswordLink = new Link("Forgot Password?", new ExternalResource("#!" + ToDoView.VIEW_NAME));
        horizontalLayout.addComponent(forgotPasswordLink);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
