package com.jtbdevelopment.e_eye_o.ria.vaadin.views;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
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
@SuppressWarnings("unused")
public class MainView extends HorizontalLayout implements View {
    public static final String VIEW_NAME = "";  // Blank so it is default

    @Autowired
    private ToDoView toDoView;

    @Autowired
    private UserRoleTestComponent userRoleTestComponent;

    @Autowired
    private ClassListComponent classListComponent;

    @PostConstruct
    public void PostConstruct() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);

        TabSheet mainMenu = new TabSheet();
        mainMenu.setWidth(null);
        mainMenu.setSizeFull();
        addComponent(mainMenu);
        setComponentAlignment(mainMenu, Alignment.TOP_CENTER);

        mainMenu.addTab(classListComponent, "Classes", new ThemeResource("../runo/icons/32/arrow-down.png"));
        /*
        mainMenu.addTab(wrapToDoView(), "Students", new ThemeResource("../runo/icons/32/arrow-down.png"));
        mainMenu.addTab(wrapToDoView(), "Observations", new ThemeResource("../runo/icons/32/arrow-down.png"));
        mainMenu.addTab(wrapToDoView(), "Photos", new ThemeResource("../runo/icons/32/arrow-down.png"));
        mainMenu.addTab(wrapToDoView(), "Observation Categories", new ThemeResource("../runo/icons/32/arrow-down.png"));
        */
        mainMenu.addTab(wrapToDoView(), "My Settings", new ThemeResource("../runo/icons/32/settings.png"));

        mainMenu.setStyleName(Reindeer.TABSHEET_MINIMAL);
        setStyleName(Reindeer.LAYOUT_BLUE);
    }

    private VerticalLayout wrapToDoView() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addComponent(toDoView);
        layout.addComponent(userRoleTestComponent);
        return layout;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }
}
