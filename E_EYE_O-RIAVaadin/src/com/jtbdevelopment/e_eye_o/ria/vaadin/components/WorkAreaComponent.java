package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.ria.vaadin.events.IdObjectRelatedSideTabClicked;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;

/**
 * Date: 3/10/13
 * Time: 4:19 PM
 */
public class WorkAreaComponent extends CustomComponent {

    //  TODO - might need to put this in a panel for scroll bar
    @AutoGenerated
    private VerticalLayout mainLayout;

    private final EventBus eventBus;

    private final ReadWriteDAO readWriteDAO;

    private final IdObjectFactory idObjectFactory;

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public WorkAreaComponent(final ReadWriteDAO readWriteDAO, final IdObjectFactory idObjectFactory, final EventBus eventBus) {
        this.eventBus = eventBus;
        this.readWriteDAO = readWriteDAO;
        this.idObjectFactory = idObjectFactory;
        eventBus.register(this);
        buildMainLayout();
        setCompositionRoot(mainLayout);

        // TODO add user code here
    }

    @AutoGenerated
    private void buildMainLayout() {
        // the main layout and components will be created here
        mainLayout = new VerticalLayout();
        mainLayout.setMargin(new MarginInfo(false, true, true, true));
        mainLayout.setSpacing(true);
        mainLayout.setSizeFull();
        setSizeFull();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void changeDataArea(final IdObjectRelatedSideTabClicked event) {
        Notification.show("Switching to " + event.getEntityType().getCaption());
        for (final Object childComponent : mainLayout) {
            mainLayout.removeComponent((Component) childComponent);
        }
        switch (event.getEntityType()) {
            case Students:
                mainLayout.addComponent(new StudentsWorkArea(readWriteDAO, idObjectFactory, eventBus, getSession().getAttribute(AppUser.class)));
                break;
            default:
                //  TODO - log or notify
        }
    }
}
