package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.VaadinView;

import javax.annotation.PostConstruct;

/**
 * Date: 3/3/13
 * Time: 9:52 PM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@VaadinView(MainView.VIEW_NAME)
@Theme(Reindeer.THEME_NAME)
public class MainView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "";  // Blank so it is default

    @PostConstruct
    public void PostConstruct() {

    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
