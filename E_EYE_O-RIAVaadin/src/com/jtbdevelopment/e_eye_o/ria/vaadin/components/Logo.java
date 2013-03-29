package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/24/13
 * Time: 3:18 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Logo extends CustomComponent {
    public Logo() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        String letters[] = {"E", "-", "E", "Y", "E", "-", "O"};
        String styles[] = {"first-e", "first-dash", "second-e", "first-y", "third-e", "second-dash", "first-o"};

        Label letter;
        for (int i = 0; i < letters.length; ++i) {
            letter = new Label(letters[i]);
            letter.addStyleName("logo-character");
            letter.addStyleName("logo-" + styles[i]);
            letter.setSizeUndefined();
            horizontalLayout.addComponent(letter);
            horizontalLayout.setComponentAlignment(letter, Alignment.MIDDLE_CENTER);
        }

        horizontalLayout.addStyleName("logo");
        horizontalLayout.setSizeUndefined();
        setCompositionRoot(horizontalLayout);
        setSizeUndefined();
    }
}
