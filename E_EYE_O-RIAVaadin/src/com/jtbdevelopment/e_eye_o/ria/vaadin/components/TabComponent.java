package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import com.jtbdevelopment.e_eye_o.ria.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs.IdObjectRelatedTab;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs.Tab;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/6/13
 * Time: 12:13 AM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TabComponent extends CustomComponent {

    public static final String SELECTED_TABS = "selected-tabs";
    public static final String LOGOUT = "Logout";
    public static final String HELP = "Help";
    public static final String SETTINGS = "Settings";
    public static final String REPORTS = "Reports";

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
            return entityType.getAnnotation(PreferredDescription.class).plural();
        }
    }

    private Label currentSelected;

    @Autowired
    public TabComponent(final EventBus eventBus) {
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
            mainLayout.addComponent(sideTab);
        }
        mainLayout.addComponent(new Tab(REPORTS, null, null));
        mainLayout.addComponent(new Tab(SETTINGS, null, null));
        mainLayout.addComponent(new Tab(HELP, null, null));
        mainLayout.addComponent(new Tab(LOGOUT, eventBus, new LogoutEvent()));

        mainLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                Component clicked = event.getChildComponent();
                if (clicked instanceof Tab) {
                    ((Tab) clicked).onClicked();
                    if (currentSelected != null) {
                        currentSelected.removeStyleName(SELECTED_TABS);
                    }
                    clicked.addStyleName(SELECTED_TABS);
                    currentSelected = (Label) clicked;
                }

            }
        });
        outerLayout.addComponent(mainLayout);
        outerLayout.setComponentAlignment(mainLayout, Alignment.MIDDLE_CENTER);
        setCompositionRoot(outerLayout);
    }
}
