package com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Date: 5/7/13
 * Time: 11:26 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - everything
public class SettingsWorkArea extends CustomComponent {

    @PostConstruct
    public void postConstruct() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setImmediate(true);
        mainLayout.setSpacing(true);

        Label label = new Label("Settings.  Under development.");
        label.setSizeUndefined();
        mainLayout.addComponent(label);
        mainLayout.setComponentAlignment(label, Alignment.MIDDLE_CENTER);
        setCompositionRoot(mainLayout);
    }
}
