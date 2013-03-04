package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.google.common.base.Joiner;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.util.SpringSecurityHelper;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 3/3/13
 * Time: 11:38 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VaadinView(UserRoleTestView.VIEW_NAME)
@Theme(Reindeer.THEME_NAME)
public class UserRoleTestView extends Panel implements View {
    public static final String VIEW_NAME = "UserRoleTestView";

    private Label usernameLabel = new Label();
    private Label rolesLabel = new Label();
    private Label lastLoginLabel = new Label();

    @PostConstruct
    public void PostConstruct() {
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.setSpacing(true);
        usernameLayout.addComponent(new Label("Username:"));
        usernameLayout.addComponent(usernameLabel);

        HorizontalLayout lastLoginLayout = new HorizontalLayout();
        lastLoginLayout.setSpacing(true);
        lastLoginLayout.addComponent(new Label("Last Login:"));
        lastLoginLayout.addComponent(lastLoginLabel);

        HorizontalLayout userRolesLayout = new HorizontalLayout();
        userRolesLayout.setSpacing(true);
        userRolesLayout.addComponent(new Label("Roles:"));
        userRolesLayout.addComponent(rolesLabel);

        layout.addComponent(usernameLayout);
        layout.addComponent(userRolesLayout);
        layout.addComponent(lastLoginLayout);

        Link userView = new Link("ROLE_USER View (disabled, if user doesn't have access)", new ExternalResource("#!" + RoleUserView.VIEW_NAME));
        Link roleView = new Link("ROLE_ADMIN View (disabled, if user doesn't have access)", new ExternalResource("#!" + RoleAdminView.VIEW_NAME));

        userView.setEnabled(SpringSecurityHelper.hasRole("ROLE_USER"));
        roleView.setEnabled(SpringSecurityHelper.hasRole("ROLE_ADMIN"));

        layout.addComponent(userView);
        layout.addComponent(roleView);
        layout.addComponent(new Link("ROLE_ADMIN View (throw exception, if user doesn't have access)", new ExternalResource("#!" + RoleAdminView.VIEW_NAME)));

        layout.addComponent(new Link("To Do Test", new ExternalResource("#!" + ToDoView.VIEW_NAME)));
        layout.addComponent(new Link("Logout", new ExternalResource("/j_spring_security_logout")));

        VerticalLayout outerLayout = new VerticalLayout();
        outerLayout.setSizeFull();
        outerLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        outerLayout.addComponent(layout);

        setContent(outerLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        AppUser appUser = getSession().getAttribute(AppUser.class);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : user.getAuthorities()) {
            roles.add(grantedAuthority.getAuthority());
        }

        usernameLabel.setValue(appUser.getEmailAddress() + "[" + appUser.getId() + "]");
        rolesLabel.setValue(Joiner.on(",").join(roles));
        lastLoginLabel.setValue(appUser.getLastLogin().toString("YYYY-MM-dd HH:mm:ss"));
    }
}
