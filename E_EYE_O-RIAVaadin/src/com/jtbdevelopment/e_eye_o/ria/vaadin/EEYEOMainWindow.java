package com.jtbdevelopment.e_eye_o.ria.vaadin;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.ria.security.authentication.events.LogoutEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Date: 2/24/13
 * Time: 8:16 PM
 */
@Theme("reindeer")
public class EEYEOMainWindow extends VerticalLayout implements View {

    private Label label;

    public EEYEOMainWindow(final EventBus eventBus) {
        label = new Label();

        addComponent(label);

        Button button = new Button("Logout");

        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                eventBus.post(new LogoutEvent());
            }
        });

        addComponent(button);
        setStyleName(Reindeer.LAYOUT_BLUE);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        label.setValue("Welcome to " + user);
    }


}
