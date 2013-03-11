package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.MainPageComposite;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TitleBarComposite;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.LogoutEvent;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Date: 3/3/13
 * Time: 11:30 AM
 */
@Component(value = "eeyeoUI")
@Scope("prototype")
@Theme("eeyeo")
@SuppressWarnings("unused")
@PreserveOnRefresh
public class EEYEOUI extends EEYEOErrorHandlingUI {
    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    private final EventBus eventBus;

    public EEYEOUI() {
        eventBus = new EventBus();
    }

    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);
        eventBus.register(this);
        final User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //  TODO
        AppUser appUser = readWriteDAO.getUser(principal.getUsername() + "@test.com");
        getSession().setAttribute(AppUser.class, appUser);

        setSizeFull();

        VerticalLayout outer = new VerticalLayout();
        outer.setSizeFull();
        outer.setMargin(true);
        setContent(outer);

        outer.addComponent(new TitleBarComposite(appUser));
        final MainPageComposite c = new MainPageComposite(readWriteDAO, idObjectFactory, eventBus);
        outer.addComponent(c);
        outer.setExpandRatio(c, 1.0f);
    }

    @Subscribe
    public void logoutEventHandler(final LogoutEvent event) {
        AppUser appUser = getSession().getAttribute(AppUser.class);
        appUser.setLastLogin(new DateTime());
        readWriteDAO.update(appUser);
        getPage().setLocation("/j_spring_security_logout");
    }
}
