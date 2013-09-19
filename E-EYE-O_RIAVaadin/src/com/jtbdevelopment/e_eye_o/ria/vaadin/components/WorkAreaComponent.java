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
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
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
public class WorkAreaComponent extends CustomComponent implements BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(WorkAreaComponent.class);

    private BeanFactory beanFactory;

    private final Panel panel = new Panel();
    private final VerticalLayout verticalLayout = new VerticalLayout();

    @Autowired
    private EventBus eventBus;

    private CustomComponent currentComponent;

    @PostConstruct
    public void postConstruct() {
        verticalLayout.setSizeFull();

        panel.setSizeFull();
        panel.setContent(verticalLayout);
        panel.addStyleName(Runo.PANEL_LIGHT);
        setCompositionRoot(panel);

        prepForTabSwitch(null);
        setSizeFull();
        eventBus.register(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void reportsArea(final ReportsClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to reports");
        Notification.show("Switching to Reports");
        prepForTabSwitch(beanFactory.getBean(ReportsWorkArea.class));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void settingsArea(final SettingsClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to settings");
        Notification.show("Switching to Settings");
        prepForTabSwitch(beanFactory.getBean(SettingsWorkArea.class));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void helpArea(final HelpClicked event) {
        if (!getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to help");
        Notification.show("Switching to Help");
        prepForTabSwitch(beanFactory.getBean(HelpWorkArea.class));
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void changeDataArea(final IdObjectRelatedSideTabClicked event) {
        if (!getUI().getSession().getAttribute(AppUser.class).equals(event.getAppUser())) {
            return;
        }
        logger.trace(getSession().getAttribute(AppUser.class).getId() + ": Switching to " + event.getEntitySettings().plural());
        Notification.show("Switching to " + event.getEntitySettings().plural());
        switch (event.getEntitySettings().plural()) {
            case "Students":
                prepForTabSwitch(beanFactory.getBean(StudentsWorkArea.class));
                break;
            case "Classes":
                prepForTabSwitch(beanFactory.getBean(ClassListsWorkArea.class));
                break;
            case "Observations":
                prepForTabSwitch(beanFactory.getBean(ObservationsWorkArea.class));
                break;
            case "Categories":
                prepForTabSwitch(beanFactory.getBean(ObservationCategoriesWorkArea.class));
                break;
            case "Photos":
                prepForTabSwitch(beanFactory.getBean(PhotosWorkArea.class));
                break;
            default:
                logger.warn("Received change data area with unknown entity type " + event.getEntitySettings().plural());
        }
    }

    private void prepForTabSwitch(final CustomComponent newComponent) {
        ComponentUtils.clearAllErrors(verticalLayout);
        if (currentComponent != null) {
            verticalLayout.removeComponent(currentComponent);
        }
        currentComponent = newComponent;
        if (currentComponent != null) {
            verticalLayout.addComponent(currentComponent);
        }
    }

    @Override
    public void detach() {
        eventBus.unregister(this);
        super.detach();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
