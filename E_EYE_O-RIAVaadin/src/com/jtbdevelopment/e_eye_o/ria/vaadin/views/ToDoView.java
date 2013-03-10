package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.PostConstruct;

/**
 * Date: 3/3/13
 * Time: 9:02 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VaadinView(ToDoView.VIEW_NAME)
public class ToDoView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "ToDo";

    @Autowired
    private ToDoComponent toDoComponent;

    @PostConstruct
    public void PostConstruct() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        addComponent(toDoComponent);
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
