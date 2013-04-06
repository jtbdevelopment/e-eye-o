package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.jtbdevelopment.e_eye_o.DAO.ReadOnlyDAO;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.ObservationCategoryHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.Logo;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;

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
    ObservationCategoryHelper observationCategoryHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ReadOnlyDAO readOnlyDAO;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private Logo logo;

    @PostConstruct
    public void PostConstruct() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        //  Title Area
        VerticalLayout titleSection = new VerticalLayout();
        addComponent(titleSection);
        setComponentAlignment(titleSection, Alignment.MIDDLE_CENTER);
        Label title = new Label("Welcome to");
        title.setWidth(null);
        title.setHeight(null);
        title.addStyleName("bold");
        titleSection.addComponent(title);
        titleSection.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        logo.addStyleName("big-logo");
        titleSection.addComponent(logo);
        titleSection.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

        //  User/Password/Button Area
        VerticalLayout loginSection = new VerticalLayout();
        addComponent(loginSection);
        setComponentAlignment(loginSection, Alignment.BOTTOM_CENTER);

        FormLayout form = new FormLayout();
        form.setWidth(null);
        form.setHeight(null);
        loginSection.addComponent(form);
        loginSection.setComponentAlignment(form, Alignment.MIDDLE_CENTER);

        form.addComponent(loginField);
        form.setComponentAlignment(loginField, Alignment.TOP_CENTER);
        form.addComponent(passwordField);
        form.setComponentAlignment(passwordField, Alignment.TOP_CENTER);

        final CheckBox rememberMe = new CheckBox("Remember Me");
        rememberMe.setValue(Boolean.TRUE);
        form.addComponent(rememberMe);
        form.setComponentAlignment(rememberMe, Alignment.TOP_CENTER);

        Button loginButton = new Button("Login");
        loginButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginButton.addStyleName(Runo.BUTTON_DEFAULT);
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
                final String hashPassword = passwordEncoder.encode(password);
                //  TODO
                final String email;
                if (login.contains("@")) {
                    email = login;
                } else {
                    email = login + "@test.com";
                }
                AppUser user = readOnlyDAO.getUser(email);
                if (user == null) {
                    //  TODO - move
                    user = readWriteDAO.create(idObjectFactory.newAppUserBuilder().withPassword(hashPassword).withEmailAddress(email).withFirstName(login).withLastName(login).withActivated(true).withActive(true).build());
                    observationCategoryHelper.createDefaultCategoriesForUser(user);
                    Map<String, ObservationCategory> map = observationCategoryHelper.getObservationCategoriesAsMap(user);
                    ClassList cl = readWriteDAO.create(idObjectFactory.newClassListBuilder(user).withDescription("Example Class").build());
                    Student s1 = readWriteDAO.create(idObjectFactory.newStudentBuilder(user).withFirstName("Student").withLastName("A").addClassList(cl).build());
                    Student s2 = readWriteDAO.create(idObjectFactory.newStudentBuilder(user).withFirstName("Student").withLastName("B").addClassList(cl).build());
                    final Iterator<Map.Entry<String, ObservationCategory>> entryIterator = map.entrySet().iterator();
                    ObservationCategory c1 = entryIterator.next().getValue();
                    ObservationCategory c2 = entryIterator.next().getValue();
                    Observation o1 = readWriteDAO.create(idObjectFactory.newObservationBuilder(user).withObservationTimestamp(new LocalDateTime().minusDays(7)).withObservationSubject(s1).withComment("Observation 1").addCategory(c1).build());
                    Observation o2 = readWriteDAO.create(idObjectFactory.newObservationBuilder(user).withObservationTimestamp(new LocalDateTime().minusDays(3)).withObservationSubject(s1).withComment("Observation 2 as Followup to 1").addCategory(c1).addCategory(c2).build());
                    readWriteDAO.linkFollowUpObservation(o1, o2);
                    readWriteDAO.create(idObjectFactory.newObservationBuilder(user).withObservationTimestamp(new LocalDateTime().minusDays(10)).withObservationSubject(s2).withFollowUpNeeded(true).withFollowUpReminder(new LocalDate().plusDays(1)).withComment("Observation 3").build());
                    readWriteDAO.create(idObjectFactory.newObservationBuilder(user).withObservationSubject(cl).withObservationTimestamp(new LocalDateTime().minusDays(1)).addCategory(c2).withComment("You can put general class observations too.").build());
                }
                try {
                    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                    if (rememberMe.getValue()) {
                        final VaadinRequest currentRequest = VaadinService.getCurrentRequest();
                        final VaadinResponse currentResponse = VaadinService.getCurrentResponse();
                        final String key = rememberMeServices.getParameter();
                        rememberMeServices.loginSuccess(new FakeRememberMeFlag(key, (VaadinServletRequest) currentRequest), (VaadinServletResponse) currentResponse, authentication);
                    }
                } catch (AuthenticationException e) {
                    Notification.show("Failed to login.", Notification.Type.ERROR_MESSAGE);
                    return;
                }
                //  TODO
                user = readOnlyDAO.getUser(email);
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

        //  User/Password/Button Area
        VerticalLayout helpSection = new VerticalLayout();
        addComponent(helpSection);
        setComponentAlignment(helpSection, Alignment.TOP_CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        Link registerLink = new Link("Register for Account", new ExternalResource("#!" + ToDoView.VIEW_NAME));
        horizontalLayout.addComponent(registerLink);
        Link forgotPasswordLink = new Link("Forgot Password?", new ExternalResource("#!" + ToDoView.VIEW_NAME));
        horizontalLayout.addComponent(forgotPasswordLink);

        helpSection.addComponent(horizontalLayout);
        helpSection.setComponentAlignment(horizontalLayout, Alignment.MIDDLE_CENTER);
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
