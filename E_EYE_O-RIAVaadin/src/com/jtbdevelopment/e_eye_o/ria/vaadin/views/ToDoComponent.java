package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
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
public class ToDoComponent extends VerticalLayout {
    @PostConstruct
    public void PostConstruct() {
        setSpacing(true);
        setMargin(true);
        setSizeFull();

        Label label = new Label("Sorry this feature is not implemented yet.");
        label.setWidth(null);
        addComponent(label);
        setComponentAlignment(label, Alignment.MIDDLE_CENTER);
    }
}
