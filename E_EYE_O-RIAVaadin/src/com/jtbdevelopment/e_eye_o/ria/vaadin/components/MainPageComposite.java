package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Date: 3/6/13
 * Time: 12:13 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MainPageComposite extends CustomComponent {

    @Autowired
    public MainPageComposite(final WorkAreaComponent workAreaComponent, final SideTabComponent sideTabComponent) {
        setSizeFull();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSizeFull();

        mainLayout.addComponent(sideTabComponent);
        mainLayout.addComponent(workAreaComponent);
        mainLayout.setExpandRatio(workAreaComponent, 1);
        setCompositionRoot(mainLayout);
    }

}
