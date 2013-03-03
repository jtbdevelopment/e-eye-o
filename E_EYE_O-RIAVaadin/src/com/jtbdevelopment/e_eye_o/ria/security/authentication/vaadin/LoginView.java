package com.jtbdevelopment.e_eye_o.ria.security.authentication.vaadin;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.events.LoginEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
public class LoginView extends HorizontalLayout implements View {
    private final TextField loginField = new TextField("Email Address");
    private final PasswordField passwordField = new PasswordField("Password");

    public LoginView(final EventBus eventBus) {
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
        verticalLayout.setComponentAlignment(title, Alignment.BOTTOM_CENTER);

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
                LoginEvent loginEvent = new LoginEvent(loginField.getValue(), passwordField.getValue());
                eventBus.post(loginEvent);
                loginField.setValue("");
                passwordField.setValue("");
            }
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        verticalLayout.addComponent(horizontalLayout);
        verticalLayout.setComponentAlignment(horizontalLayout, Alignment.TOP_CENTER);
        Button registerButton = new Button("Create Account?");
        registerButton.setStyleName(Reindeer.BUTTON_LINK);
        horizontalLayout.addComponent(registerButton);
        Button forgotPassword = new Button("Forgot Password?");
        forgotPassword.setStyleName(Reindeer.BUTTON_LINK);
        horizontalLayout.addComponent(forgotPassword);

        setStyleName(Reindeer.LAYOUT_BLUE);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
