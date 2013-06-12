package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 6/11/13
 * Time: 6:42 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - everything
public class UserPreferences extends CustomComponent {
    @PostConstruct
    public void postConstruct() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();

        Label todo = new Label("Not implemented yet.");
        todo.setSizeUndefined();
        layout.addComponent(todo);
        layout.setComponentAlignment(todo, Alignment.MIDDLE_CENTER);

        setSizeFull();
        setCompositionRoot(layout);
    }
}
