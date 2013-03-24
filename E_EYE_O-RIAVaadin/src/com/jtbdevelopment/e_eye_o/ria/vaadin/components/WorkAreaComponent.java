package com.jtbdevelopment.e_eye_o.ria.vaadin.components;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jtbdevelopment.e_eye_o.ria.events.IdObjectRelatedSideTabClicked;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas.ClassListsWorkArea;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas.ObservationsWorkArea;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.workareas.StudentsWorkArea;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;

/**
 * Date: 3/10/13
 * Time: 4:19 PM
 */
@org.springframework.stereotype.Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkAreaComponent extends CustomComponent implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final Panel mainLayout;

    @Autowired
    public WorkAreaComponent(final EventBus eventBus) {
        eventBus.register(this);
        mainLayout = new Panel();
        mainLayout.addStyleName(Runo.PANEL_LIGHT);
        mainLayout.setSizeFull();
        setSizeFull();
        setCompositionRoot(mainLayout);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void changeDataArea(final IdObjectRelatedSideTabClicked event) {
        Notification.show("Switching to " + event.getEntityType().getCaption());
        mainLayout.setContent(null);
        switch (event.getEntityType()) {
            case Students:
                mainLayout.setContent(applicationContext.getBean(StudentsWorkArea.class));
                break;
            case Classes:
                mainLayout.setContent(applicationContext.getBean(ClassListsWorkArea.class));
                break;
            case Observations:
                mainLayout.setContent(applicationContext.getBean(ObservationsWorkArea.class));
                break;
            default:
                //  TODO - log or notify
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
