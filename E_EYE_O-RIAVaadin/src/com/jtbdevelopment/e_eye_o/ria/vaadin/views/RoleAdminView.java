package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.PostConstruct;

/**
 * Date: 3/3/13
 * Time: 11:40 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VaadinView(RoleAdminView.VIEW_NAME)
@Secured("ROLE_ADMIN")
public class RoleAdminView extends Panel implements View
{
    public static final String VIEW_NAME = "RoleAdmin";

    @PostConstruct
    public void PostConstruct()
    {
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        layout.addComponent(new Label("ROLE_ADMIN"));
        layout.addComponent(new Link("Go back", new ExternalResource("#!" + UserRoleTestView.VIEW_NAME)));

        setContent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
    }
}
