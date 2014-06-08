package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/5/13
 * Time: 11:23 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TitleBarComposite extends CustomComponent {

    @Autowired
    public TitleBarComposite(final Logo logo, final TabComponent tabComponent) {
        setWidth(100, Unit.PERCENTAGE);
        setHeight(null);
        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();

        mainLayout.addComponent(logo);
        mainLayout.addComponent(logo);
        mainLayout.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);

        mainLayout.addComponent(tabComponent);
        mainLayout.setComponentAlignment(tabComponent, Alignment.MIDDLE_CENTER);
        mainLayout.setExpandRatio(tabComponent, 1.0f);


        mainLayout.addStyleName("titlebar");
        setCompositionRoot(mainLayout);
    }
}
