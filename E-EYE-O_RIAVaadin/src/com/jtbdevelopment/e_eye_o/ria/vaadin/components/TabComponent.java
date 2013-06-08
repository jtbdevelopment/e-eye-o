package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.ria.events.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.tabs.IdObjectRelatedTab;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.tabs.Tab;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 3/6/13
 * Time: 12:13 AM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TabComponent extends CustomComponent {
    //  TODO - move this?
    public enum IdObjectTabs {
        Students(Student.class),
        Observations(Observation.class),
        Photos(Photo.class),
        Classes(ClassList.class),
        Categories(ObservationCategory.class);

        IdObjectTabs(final Class<? extends AppUserOwnedObject> entityType) {
            this.entityType = entityType;
        }

        private final Class<? extends AppUserOwnedObject> entityType;

        public String getCaption() {
            return entityType.getAnnotation(IdObjectEntitySettings.class).plural();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(TabComponent.class);

    public static final String SELECTED_TABS = "selected-tabs";
    public static final String LOGOUT = "Logout";
    public static final String HELP = "Help";
    public static final String REPORTS = "Reports";
    private List<IdObjectRelatedTab> objectTabs = new LinkedList<>();
    private Tab reportTab;
    private Tab logoutTab;
    private Tab helpTab;
    private Tab userTab;

    private Label currentSelected;

    @Autowired
    private EventBus eventBus;

    @Value("${email.contactus}")
    private String contactEmail;

    @PostConstruct
    public void postConstruct() {
        HorizontalLayout outerLayout = new HorizontalLayout();
        outerLayout.setWidth(100, Unit.PERCENTAGE);

        CssLayout mainLayout = new CssLayout();
        mainLayout.setSizeUndefined();

        for (IdObjectTabs entityType : IdObjectTabs.values()) {
            final IdObjectRelatedTab sideTab = new IdObjectRelatedTab(entityType, eventBus);
            if (IdObjectTabs.Students.equals(entityType)) {  //  TODO - config
                currentSelected = sideTab;
                sideTab.addStyleName(SELECTED_TABS);
            }
            objectTabs.add(sideTab);
            mainLayout.addComponent(sideTab);
        }
        reportTab = new Tab(REPORTS, eventBus);
        mainLayout.addComponent(reportTab);

        CssLayout sideLayout = new CssLayout();
        sideLayout.setSizeUndefined();
        userTab = new Tab("Welcome", eventBus);
        userTab.setDescription("Change settings.");
        sideLayout.addComponent(userTab);
        logoutTab = new Tab(LOGOUT, eventBus);
        sideLayout.addComponent(logoutTab);
        helpTab = new Tab(HELP, eventBus);
        sideLayout.addComponent(helpTab);

        Link email = new Link("Contact us!", new ExternalResource("mailto:" + contactEmail));
        email.setSizeUndefined();
        email.addStyleName("tabs");
        sideLayout.addComponent(email);

        ClickListener clickListener = new ClickListener();
        mainLayout.addLayoutClickListener(clickListener);
        sideLayout.addLayoutClickListener(clickListener);
        outerLayout.addComponent(mainLayout);
        outerLayout.setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        outerLayout.setExpandRatio(mainLayout, 1);
        outerLayout.addComponent(sideLayout);
        outerLayout.setComponentAlignment(sideLayout, Alignment.MIDDLE_RIGHT);
        setCompositionRoot(outerLayout);
    }

    @Override
    public void attach() {
        super.attach();
        AppUser appUser = getUI().getSession().getAttribute(AppUser.class);
        userTab.setValue("Welcome " + appUser.getFirstName());
        userTab.setMessageToPublish(new SettingsClicked(appUser));
        reportTab.setMessageToPublish(new ReportsClicked(appUser));
        logoutTab.setMessageToPublish(new LogoutEvent(appUser));
        helpTab.setMessageToPublish(new HelpClicked(appUser));
        for (IdObjectRelatedTab tab : objectTabs) {
            tab.setMessageToPublish(new IdObjectRelatedSideTabClicked(appUser, tab.getIdObjectTab()));
        }
    }

    private class ClickListener implements LayoutEvents.LayoutClickListener {
        @Override
        public void layoutClick(final com.vaadin.event.LayoutEvents.LayoutClickEvent event) {
            Component clicked = event.getChildComponent();
            if (clicked instanceof Tab) {
                Tab clickedTab = (Tab) clicked;
                logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Clicked on " + clickedTab.getCaption());
                clickedTab.onClicked();
                if (currentSelected != null) {
                    currentSelected.removeStyleName(SELECTED_TABS);
                }
                clicked.addStyleName(SELECTED_TABS);
                currentSelected = (Label) clicked;
            }
        }
    }
}
