package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.events.HelpClicked;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectRelatedSideTabClicked;
import com.jtbdevelopment.e_eye_o.ria.events.ReportsClicked;
import com.jtbdevelopment.e_eye_o.ria.events.SettingsClicked;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas.*;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * Date: 3/10/13
 * Time: 4:19 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkAreaComponent extends CustomComponent {
    private static final Logger logger = LoggerFactory.getLogger(WorkAreaComponent.class);

    private final Panel panel = new Panel();
    private final VerticalLayout verticalLayout = new VerticalLayout();

    @Autowired
    private StudentsWorkArea studentsWorkArea;
    @Autowired
    private ClassListsWorkArea classListsWorkArea;
    @Autowired
    private ObservationsWorkArea observationsWorkArea;
    @Autowired
    private PhotosWorkArea photosWorkArea;
    @Autowired
    private ObservationCategoriesWorkArea observationCategoriesWorkArea;
    @Autowired
    private HelpWorkArea helpWorkArea;
    @Autowired
    private SettingsWorkArea settingsWorkArea;
    @Autowired
    private ReportsWorkArea reportsWorkArea;

    @Autowired
    private EventBus eventBus;

    @PostConstruct
    public void postConstruct() {
        verticalLayout.setSizeFull();
        verticalLayout.addComponent(studentsWorkArea);
        verticalLayout.addComponent(classListsWorkArea);
        verticalLayout.addComponent(observationCategoriesWorkArea);
        verticalLayout.addComponent(observationsWorkArea);
        verticalLayout.addComponent(photosWorkArea);
        verticalLayout.addComponent(helpWorkArea);
        verticalLayout.addComponent(settingsWorkArea);
        verticalLayout.addComponent(reportsWorkArea);

        panel.setSizeFull();
        panel.setContent(verticalLayout);
        panel.addStyleName(Runo.PANEL_LIGHT);
        setCompositionRoot(panel);

        prepForTabSwitch();
        studentsWorkArea.setVisible(true);               //  TODO - make which one a default
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void reportsArea(final ReportsClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to reports");
        Notification.show("Switching to Reports");
        prepForTabSwitch();
        reportsWorkArea.setVisible(true);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void settingsArea(final SettingsClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to settings");
        Notification.show("Switching to Settings");
        prepForTabSwitch();
        settingsWorkArea.setVisible(true);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void helpArea(final HelpClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to help");
        Notification.show("Switching to Help");
        prepForTabSwitch();
        helpWorkArea.setVisible(true);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void changeDataArea(final IdObjectRelatedSideTabClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to " + event.getEntityType());
        Notification.show("Switching to " + event.getEntityType().getCaption());
        prepForTabSwitch();
        switch (event.getEntityType()) {
            case Students:
                studentsWorkArea.setVisible(true);
                break;
            case Classes:
                classListsWorkArea.setVisible(true);
                break;
            case Observations:
                observationsWorkArea.setVisible(true);
                break;
            case Categories:
                observationCategoriesWorkArea.setVisible(true);
                break;
            case Photos:
                photosWorkArea.setVisible(true);
                break;
            default:
                logger.warn("Received change data area with unknown entity type " + event.getEntityType());
        }
    }

    private void prepForTabSwitch() {
        ComponentUtils.clearAllErrors(verticalLayout);
        for (Component child : verticalLayout) {
            child.setVisible(false);
        }
    }

    @Override
    public void attach() {
        super.attach();
        eventBus.register(this);
    }

    @Override
    public void detach() {
        eventBus.unregister(this);
        super.detach();
    }
}
