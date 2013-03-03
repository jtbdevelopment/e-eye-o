package com.jtbdevelopment.e_eye_o.ria.security.authentication.vaadin;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.events.LoginEvent;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.events.StyleEvent;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.spring.AuthenticationService;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.util.RequestHolder;
import com.jtbdevelopment.e_eye_o.ria.vaadin.EEYEOMainWindow;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import static com.vaadin.ui.Notification.TYPE_ERROR_MESSAGE;

/**
 * Date: 2/24/13
 * Time: 8:24 PM
 * <p/>
 * Largely based on Spring Security Example by Nicolas Frankel at
 * https://github.com/nfrankel/More-Vaadin/tree/master/springsecurity-integration
 */
@Theme("reindeer")
public class SecuredRoot extends UI {

    private static final String MAIN_VIEW_NAME = "main";

    private static final String LOGIN_VIEW_NAME = "login";

    private EventBus bus;

    private Navigator navigator;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    private AppUser appUser;

    private SpringBridge springBridge = new SpringBridge();

    @Override
    protected void init(final VaadinRequest request) {
        getPage().setTitle("E-EYE-O");
        navigator = new Navigator(this, this);
        navigator.addViewChangeListener(new ViewChangeSecurityChecker());

        bus = new EventBus();

        navigator.addView(LOGIN_VIEW_NAME, new LoginView(bus));
        navigator.addView(MAIN_VIEW_NAME, new EEYEOMainWindow(bus));

        navigator.navigateTo(LOGIN_VIEW_NAME);

        bus.register(this);

    }

    @Subscribe
    public void login(LoginEvent event) {

        AuthenticationService authHandler = new AuthenticationService();

        readWriteDAO = SpringBridge.getBean(ReadWriteDAO.class);
        idObjectFactory = SpringBridge.getBean(IdObjectFactory.class);

        try {
            authHandler.handleAuthentication(event.getLogin(), event.getPassword(), RequestHolder.getRequest());
            for (AppUser user : readWriteDAO.getUsers()) {
                if (user.getEmailAddress().equals(event.getLogin() + "@test.com")) {
                    appUser = user;
                    appUser.setLastLogin(new DateTime());
                    readWriteDAO.update(appUser);
                    break;
                }
            }
            if (appUser == null) {
                final AppUser entity = idObjectFactory.newAppUser().setEmailAddress(event.getLogin() + "@test.com").setFirstName(event.getLogin()).setLastName(event.getLogin());
                appUser = readWriteDAO.create(entity);
            }

            navigator.navigateTo(MAIN_VIEW_NAME);

        } catch (BadCredentialsException e) {

            Notification.show("Bad credentials", TYPE_ERROR_MESSAGE);
        }
    }

    @Subscribe
    public void logout(LogoutEvent event) {

        AuthenticationService authHandler = new AuthenticationService();

        authHandler.handleLogout(RequestHolder.getRequest());

        navigator.navigateTo(LOGIN_VIEW_NAME);
        appUser = null;
    }

    @Subscribe
    public void theme(StyleEvent event) {
        setStyleName(event.getStyle());
    }

}
