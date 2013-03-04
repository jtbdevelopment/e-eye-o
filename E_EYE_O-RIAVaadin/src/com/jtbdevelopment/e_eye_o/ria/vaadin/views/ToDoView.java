package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 3/3/13
 * Time: 9:02 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ToDoView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "ToDo";

    @PostConstruct
    public void PostConstruct() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        Label label = new Label("Sorry this feature is not implemented yet.");
        label.setWidth(null);
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        setStyleName(Reindeer.LAYOUT_BLUE);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
