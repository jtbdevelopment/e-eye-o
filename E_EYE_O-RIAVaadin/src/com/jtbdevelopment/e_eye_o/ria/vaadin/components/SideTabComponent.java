package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import com.jtbdevelopment.e_eye_o.ria.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs.BlankSideTab;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs.IdObjectRelatedSideTab;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.sidetabs.SideTab;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/6/13
 * Time: 12:13 AM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SideTabComponent extends CustomComponent {

    public static final String SELECTED_SIDETABS = "selected-sidetabs";

    //  TODO - move this?
    public enum IdObjectSideTabs {
        Students(Student.class),
        Observations(Observation.class),
        Photos(Photo.class),
        Classes(ClassList.class),
        Categories(ObservationCategory.class);

        IdObjectSideTabs(final Class<? extends AppUserOwnedObject> entityType) {
            this.entityType = entityType;
        }

        private final Class<? extends AppUserOwnedObject> entityType;

        public String getCaption() {
            return entityType.getAnnotation(PreferredDescription.class).plural();
        }
    }

    private Label currentSelected;

    @Autowired
    public SideTabComponent(final EventBus eventBus) {
        CssLayout mainLayout = new CssLayout();
        mainLayout.setSizeUndefined();
        mainLayout.setHeight(null);
        mainLayout.setWidth(getSideTabWidthValue(), getSideTabWidthUnits());

        for (IdObjectSideTabs entityType : IdObjectSideTabs.values()) {
            final IdObjectRelatedSideTab sideTab = new IdObjectRelatedSideTab(entityType, eventBus);
            if (IdObjectSideTabs.Students.equals(entityType)) {  //  TODO - config
                currentSelected = sideTab;
                sideTab.addStyleName(SELECTED_SIDETABS);
            }
            mainLayout.addComponent(sideTab);
        }
        mainLayout.addComponent(new BlankSideTab());
        mainLayout.addComponent(new SideTab("Reports", null, null));
        mainLayout.addComponent(new BlankSideTab());
        mainLayout.addComponent(new SideTab("Settings", null, null));
        mainLayout.addComponent(new BlankSideTab());
        mainLayout.addComponent(new SideTab("Help", null, null));
        mainLayout.addComponent(new BlankSideTab());
        mainLayout.addComponent(new SideTab("Logout", eventBus, new LogoutEvent()));

        mainLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                Component clicked = event.getChildComponent();
                if (clicked instanceof SideTab) {
                    ((SideTab) clicked).onClicked();
                    if (currentSelected != null) {
                        currentSelected.removeStyleName(SELECTED_SIDETABS);
                    }
                    clicked.addStyleName(SELECTED_SIDETABS);
                    currentSelected = (Label) clicked;
                }

            }
        });
        setCompositionRoot(mainLayout);
        setWidth(getSideTabWidthValue(), getSideTabWidthUnits());
    }

    private Unit getSideTabWidthUnits() {
        return Unit.EM;
    }

    private float getSideTabWidthValue() {
        return (float) (IdObjectSideTabs.Observations.getCaption().length() - 3.5);
    }
}
