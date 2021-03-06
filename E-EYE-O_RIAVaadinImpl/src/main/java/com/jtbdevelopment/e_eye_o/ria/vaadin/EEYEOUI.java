package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserMaintenanceHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.events.IdObjectChanged;
import com.jtbdevelopment.e_eye_o.ria.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TitleBarComposite;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.WorkAreaComponent;
import com.jtbdevelopment.e_eye_o.security.AppUserUserDetails;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final Logger logger = LoggerFactory.getLogger(EEYEOUI.class);

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private EventBus eventBus;

    @Autowired
    private WorkAreaComponent workAreaComponent;

    @Autowired
    private TitleBarComposite titleBarComposite;

    @Autowired
    private UserMaintenanceHelper userMaintenanceHelper;

    // TODO - determine if new legal info needs to be shown
    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);
        eventBus.register(this);
        final Object principalAsObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principalAsObject instanceof AppUserUserDetails) {
            AppUser appUser = ((AppUserUserDetails) principalAsObject).getAppUser();
            getSession().setAttribute(AppUser.class, appUser);
            AppUserSettings setting = userMaintenanceHelper.getUserSettings(appUser);
            getSession().setAttribute(AppUserSettings.class, setting);
        } else {
            throw new RuntimeException("Invalid Principal Object");
        }

        setSizeFull();

        VerticalLayout outer = new VerticalLayout();
        outer.setSizeFull();
        outer.setMargin(new MarginInfo(true, true, true, false));

        outer.addComponent(titleBarComposite);
        outer.addComponent(workAreaComponent);
        outer.setExpandRatio(workAreaComponent, 1.0f);

        setContent(outer);
    }

    @Override
    public void close() {
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": EEYEOUI.close called.");
        logoutEventHandler(null);
        super.close();
    }

    @Subscribe
    public void logoutEventHandler(final LogoutEvent event) {
        AppUser appUser = getSession().getAttribute(AppUser.class);
        if (event != null && appUser.equals(event.getAppUser())) {
            logger.trace(appUser.getId() + ": logoutEventHandlerCalled.");
            readWriteDAO.updateAppUserLogout(appUser);
            getPage().setLocation("/j_spring_security_logout");
        }
    }

    @Subscribe
    public void deleteUserHandler(final IdObjectChanged idObjectChanged) {
        AppUser appUser = getSession().getAttribute(AppUser.class);
        if (AppUser.class.isAssignableFrom(idObjectChanged.getEntityType())) {
            if (appUser.equals(idObjectChanged.getEntity())) {
                if (IdObjectChanged.ChangeType.DELETED.equals(idObjectChanged.getChangeType())) {
                    logger.trace(appUser.getId() + ": deleteUserHandler called.");
                    getPage().setLocation("/j_spring_security_logout");
                } else {
                    getSession().setAttribute(AppUser.class, (AppUser) idObjectChanged.getEntity());
                }
            }
        }
        if (AppUserSettings.class.isAssignableFrom(idObjectChanged.getEntityType())) {
            AppUserSettings settings = (AppUserSettings) idObjectChanged.getEntity();
            if (appUser.equals(settings.getAppUser())) {
                getSession().setAttribute(AppUserSettings.class, settings);
            }
        }
    }
}
