package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.MainPageComposite;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TitleBarComposite;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Date: 3/3/13
 * Time: 11:30 AM
 */
@Component(value = "eeyeoUI")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Theme("eeyeo")
@SuppressWarnings("unused")
@PreserveOnRefresh
public class EEYEOUI extends EEYEOErrorHandlingUI {
    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private MainPageComposite mainPageComposite;

    @Autowired
    private TitleBarComposite titleBarComposite;

    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);
        eventBus.register(this);
        final User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //  TODO
        AppUser appUser = readWriteDAO.getUser(principal.getUsername() + "@test.com");
        getSession().setAttribute(AppUser.class, appUser);

        //  TODO - clean up after fixing other dev comp
        /*
for(Observable observable : readWriteDAO.getEntitiesForUser(Observable.class, appUser)) {
    observable.setLastObservationTimestamp(readWriteDAO.getLastObservationTimestampForEntity(observable));
    readWriteDAO.update(observable);
}
*/
        setSizeFull();

        VerticalLayout outer = new VerticalLayout();
        outer.setSizeFull();
        outer.setMargin(new MarginInfo(true, true, true, false));

        outer.addComponent(titleBarComposite);
        outer.addComponent(mainPageComposite);
        outer.setExpandRatio(mainPageComposite, 1.0f);

        setContent(outer);
    }

    @Override
    public void close() {
        logoutEventHandler(null);
        super.close();
    }

    @Subscribe
    public void logoutEventHandler(final LogoutEvent event) {
        AppUser appUser = getSession().getAttribute(AppUser.class);
        appUser.setLastLogout(new DateTime());
        readWriteDAO.update(appUser);
        getPage().setLocation("/j_spring_security_logout");
    }
}
