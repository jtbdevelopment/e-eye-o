package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.LogoutEvent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.widgets.sidetabs.BlankSideTab;
import com.jtbdevelopment.e_eye_o.ria.vaadin.widgets.sidetabs.IdObjectRelatedSideTab;
import com.jtbdevelopment.e_eye_o.ria.vaadin.widgets.sidetabs.SideTab;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

/**
 * Date: 3/6/13
 * Time: 12:13 AM
 */
public class SideTabComponent extends CustomComponent {
    //  TODO - move this?
    public enum IdObjectSideTabs {
        //  TODO - make descriptions part of classes?
        Students("Students", Student.class),
        Observations("Observations", Observation.class),
        Classes("Classes", ClassList.class),
        Categories("Categories", ObservationCategory.class),
        Photos("Photos", Photo.class);

        IdObjectSideTabs(final String caption, final Class<? extends AppUserOwnedObject> entityType) {
            this.entityType = entityType;
            this.caption = caption;
        }

        private final Class<? extends AppUserOwnedObject> entityType;
        private final String caption;

        public Class<? extends AppUserOwnedObject> getEntityType() {
            return entityType;
        }

        public String getCaption() {
            return caption;
        }
    }

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public SideTabComponent(final EventBus eventBus) {
        CssLayout mainLayout = new CssLayout();
        mainLayout.setHeight(null);
        mainLayout.setWidth(IdObjectSideTabs.Observations.getCaption().length(), Unit.EM);

        for (IdObjectSideTabs entityType : IdObjectSideTabs.values()) {
            mainLayout.addComponent(new IdObjectRelatedSideTab(entityType, eventBus));
        }
        mainLayout.addComponent(new BlankSideTab());
        mainLayout.addComponent(new SideTab("Settings", null, null));
        mainLayout.addComponent(new BlankSideTab());
        mainLayout.addComponent(new SideTab("Logout", eventBus, new LogoutEvent()));
        mainLayout.addComponent(new BlankSideTab());

        mainLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            private Label currentSelected;

            @Override
            public void layoutClick(final LayoutEvents.LayoutClickEvent event) {
                Component clicked = event.getChildComponent();
                if (clicked instanceof SideTab) {
                    ((SideTab) clicked).onClicked();
                    if (currentSelected != null) {
                        currentSelected.removeStyleName("selected-sidetabs");
                    }
                    clicked.addStyleName("selected-sidetabs");
                    currentSelected = (Label) clicked;
                }

            }
        });
        setCompositionRoot(mainLayout);
    }
}
