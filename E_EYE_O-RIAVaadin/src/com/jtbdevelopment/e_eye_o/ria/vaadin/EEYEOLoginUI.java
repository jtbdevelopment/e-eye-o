package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.jtbdevelopment.e_eye_o.ria.vaadin.views.LoginView;
import com.jtbdevelopment.e_eye_o.ria.vaadin.views.ToDoView;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/3/13
 * Time: 11:30 AM
 */
@Component(value = "eeyeoLoginUI")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Theme("eeyeo")
@SuppressWarnings("unused")
public class EEYEOLoginUI extends EEYEOErrorHandlingUI {
    @Autowired
    private LoginView loginView;

    @Autowired
    private ToDoView toDoView;

    @Override
    protected void init(final VaadinRequest request) {
        super.init(request);

        setSizeFull();

        Navigator navigator = new Navigator(this, this);
        navigator.addView(LoginView.VIEW_NAME, loginView);
        navigator.addView(ToDoView.VIEW_NAME, toDoView);
        navigator.navigateTo(LoginView.VIEW_NAME);
    }

}
