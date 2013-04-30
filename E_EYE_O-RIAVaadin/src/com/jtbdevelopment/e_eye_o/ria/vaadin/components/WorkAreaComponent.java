package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectRelatedSideTabClicked;
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
    private EventBus eventBus;

    @PostConstruct
    public void postConstruct() {
        verticalLayout.addComponent(studentsWorkArea);
        verticalLayout.addComponent(classListsWorkArea);
        verticalLayout.addComponent(observationCategoriesWorkArea);
        verticalLayout.addComponent(observationsWorkArea);
        verticalLayout.addComponent(photosWorkArea);
        photosWorkArea.setVisible(false);
        studentsWorkArea.setVisible(true);               //  TODO - make which one a default
        classListsWorkArea.setVisible(false);
        observationCategoriesWorkArea.setVisible(false);
        observationsWorkArea.setVisible(false);
        verticalLayout.setSizeFull();
        panel.setSizeFull();
        panel.setContent(verticalLayout);
        panel.addStyleName(Runo.PANEL_LIGHT);
        setCompositionRoot(panel);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void changeDataArea(final IdObjectRelatedSideTabClicked event) {
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to " + event.getEntityType());
        Notification.show("Switching to " + event.getEntityType().getCaption());
        ComponentUtils.clearAllErrors(verticalLayout);
        for (Component child : verticalLayout) {
            child.setVisible(false);
        }
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
