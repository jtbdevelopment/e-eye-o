package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.ToDoView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.Reindeer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.DiscoveryNavigator;

/**
 * Date: 3/3/13
 * Time: 11:30 AM
 */
@Component(value = "eeyeoUI")
@Scope("prototype")
@Theme(Reindeer.THEME_NAME)
@SuppressWarnings("unused")
@PreserveOnRefresh
public class EEYEOUI extends EEYEOErrorHandlingUI {
    @Autowired
    private ToDoView toDoView;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);
        final User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //  TODO
        AppUser appUser = readWriteDAO.getUser(principal.getUsername() + "@test.com");
        appUser = readWriteDAO.update(appUser.setLastLogin(new DateTime()));
        getSession().setAttribute(AppUser.class, appUser);

        setSizeFull();

        DiscoveryNavigator navigator = new DiscoveryNavigator(this, this);
        navigator.addView(ToDoView.VIEW_NAME, toDoView);
        navigator.navigateTo(UI.getCurrent().getPage().getUriFragment());

        getSession().setAttribute(AppUser.class, appUser);
    }
}
