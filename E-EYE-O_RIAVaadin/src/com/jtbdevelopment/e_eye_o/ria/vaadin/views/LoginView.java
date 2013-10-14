package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.quicktour.QuickTour;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.passwordreset.ResetRequest;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.registration.LegalView;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequestWrapper;

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

    @Value("${url.root}")
    private String urlRoot;

    public static final String VIEW_NAME = "Login";

    private final TextField loginField = new TextField("Email Address");
    private final PasswordField passwordField = new PasswordField("Password");

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private Logo logo;

    @Autowired
    private QuickTour quickTour;

    @Value("${email.contactus}")
    private String contact;

    @PostConstruct
    public void PostConstruct() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        Panel panel = new Panel();
        panel.setSizeFull();
        panel.addStyleName(Runo.PANEL_LIGHT);

        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setSizeFull();
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeUndefined();
        Layout titleSection = getTitleSection();
        verticalLayout.addComponent(titleSection);
        verticalLayout.setComponentAlignment(titleSection, Alignment.MIDDLE_CENTER);

        Layout loginSection = getLoginSection();
        verticalLayout.addComponent(loginSection);
        verticalLayout.setComponentAlignment(loginSection, Alignment.MIDDLE_CENTER);

        Layout helpSection = getSignUpResetSection();
        verticalLayout.addComponent(helpSection);
        verticalLayout.setComponentAlignment(helpSection, Alignment.MIDDLE_CENTER);
        outerLayout.addComponent(verticalLayout);
        outerLayout.setComponentAlignment(verticalLayout, Alignment.MIDDLE_CENTER);
        panel.setContent(outerLayout);

        addComponent(panel);
        ComponentUtils.setImmediateForAll(this, true);
        ComponentUtils.setTextFieldWidths(this, 20, Unit.EM);
    }

    private Layout getLoginSection() {
        VerticalLayout loginSection = new VerticalLayout();

        FormLayout form = new FormLayout();
        form.setSizeUndefined();
        loginSection.addComponent(form);
        loginSection.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        form.addComponent(loginField);
        form.setComponentAlignment(loginField, Alignment.MIDDLE_CENTER);
        form.addComponent(passwordField);
        form.setComponentAlignment(passwordField, Alignment.MIDDLE_CENTER);

        final CheckBox rememberMe = new CheckBox("Remember Me");
        rememberMe.setValue(Boolean.FALSE);
        loginSection.addComponent(rememberMe);
        loginSection.setComponentAlignment(rememberMe, Alignment.MIDDLE_CENTER);

        Button loginButton = new Button("Login");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addStyleName(Runo.BUTTON_DEFAULT);
        loginSection.addComponent(loginButton);
        loginSection.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
        loginButton.addStyleName("primary");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Authentication authentication;
                final String login = loginField.getValue();
                final String password = passwordField.getValue();
                try {
                    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
                    if (rememberMe.getValue()) {
                        final VaadinRequest currentRequest = VaadinService.getCurrentRequest();
                        final VaadinResponse currentResponse = VaadinService.getCurrentResponse();
                        final String key = rememberMeServices.getParameter();
                        rememberMeServices.loginSuccess(new FakeRememberMeFlag(key, (VaadinServletRequest) currentRequest), (VaadinServletResponse) currentResponse, authentication);
                    }
                } catch (AuthenticationException e) {
                    //  TODO - lock account after X attempts
                    Notification.show("Failed to login.  Username or password does not match an active account.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                AppUser user = readOnlyDAO.getUser(login);
                if (user == null) {
                    Notification.show("This is embarrassing", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                SecurityContextHolder.getContext().setAuthentication(authentication);
                getParent().getUI().getSession().close();
                getParent().getUI().getPage().setLocation(urlRoot);
                loginField.setValue("");
                passwordField.setValue("");
            }
        });
        return loginSection;
    }

    private Layout getSignUpResetSection() {
        VerticalLayout helpSection = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);

        helpSection.addComponent(new Label("<br/>", ContentMode.HTML));

        Link registerLink = new Link("Register for Account", new ExternalResource("#!" + LegalView.VIEW_NAME));
        horizontalLayout.addComponent(registerLink);
        Link forgotPasswordLink = new Link("Forgot Password?", new ExternalResource("#!" + ResetRequest.VIEW_NAME));
        horizontalLayout.addComponent(forgotPasswordLink);
        helpSection.addComponent(horizontalLayout);
        helpSection.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);

        helpSection.addComponent(new Label("<br/>", ContentMode.HTML));

        Label description = new Label("<H2>Electronic Early Years Educator's Observations</H2>", ContentMode.HTML);
        description.addStyleName("bold");
        helpSection.addComponent(description);
        helpSection.setComponentAlignment(description, Alignment.MIDDLE_CENTER);

        final Button tour = new Button("Take the Quick Tour!");
        tour.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window window = new Window();
                Panel panel = new Panel();
                panel.setSizeFull();
                panel.addStyleName(Runo.PANEL_LIGHT);

                window.setContent(quickTour);
                window.setSizeFull();
                window.setModal(true);
                window.setCloseShortcut(ShortcutAction.KeyCode.ESCAPE);

                tour.getUI().addWindow(window);
            }
        });
        helpSection.addComponent(tour);
        helpSection.setComponentAlignment(tour, Alignment.MIDDLE_CENTER);

        helpSection.addComponent(new Label("<br/>", ContentMode.HTML));

        Link link = new Link("Contact Us!", new ExternalResource("mailto:" + contact));
        link.setSizeUndefined();
        helpSection.addComponent(link);
        helpSection.setComponentAlignment(link, Alignment.MIDDLE_CENTER);
        return helpSection;
    }

    private Layout getTitleSection() {
        VerticalLayout titleSection = new VerticalLayout();
        Label title = new Label("<H2>Welcome To</H2>", ContentMode.HTML);
        title.setSizeUndefined();
        title.addStyleName("bold");
        titleSection.addComponent(title);
        titleSection.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        logo.setBigLogo();
        titleSection.addComponent(logo);
        titleSection.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        return titleSection;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        getUI().setFocusedComponent(loginField);
    }

    private static class FakeRememberMeFlag extends HttpServletRequestWrapper {
        private final String rememberMeParameter;

        private FakeRememberMeFlag(final String rememberMeParameter, final VaadinServletRequest vaadinServletRequest) {
            super(vaadinServletRequest);
            this.rememberMeParameter = rememberMeParameter;
        }

        @Override
        public String getParameter(final String name) {
            if (rememberMeParameter.equals(name))
                return "1";
            return super.getParameter(name);
        }
    }
}
