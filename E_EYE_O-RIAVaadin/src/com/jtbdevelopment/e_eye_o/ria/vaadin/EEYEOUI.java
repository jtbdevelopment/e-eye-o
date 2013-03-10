package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TitleBarComposite;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.WorkAreaComposite;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
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
@Theme(Runo.THEME_NAME)
@SuppressWarnings("unused")
@PreserveOnRefresh
public class EEYEOUI extends EEYEOErrorHandlingUI {
    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectFactory idObjectFactory;

    //@Autowired
    //private TitleBarComposite titleBarComposite;

    //@Autowired
    //private WorkAreaComposite workAreaComposite;

    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);
        final User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //  TODO
        AppUser appUser = readWriteDAO.getUser(principal.getUsername() + "@test.com");
        appUser.setLastLogin(new DateTime());
        appUser = readWriteDAO.update(appUser);
        getSession().setAttribute(AppUser.class, appUser);

        setSizeFull();

        VerticalLayout outer = new VerticalLayout();
        outer.setSizeFull();
        outer.setMargin(true);
        setContent(outer);

        outer.addComponent(new TitleBarComposite(appUser));
        final WorkAreaComposite c = new WorkAreaComposite(readWriteDAO, idObjectFactory, appUser);
        outer.addComponent(c);
        outer.setExpandRatio(c, 1.0f);

        getSession().setAttribute(AppUser.class, appUser);
    }
}
