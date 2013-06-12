package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Date: 6/11/13
 * Time: 6:42 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  TODO - everything
public class UserPreferences extends CustomComponent {
    @Resource(name = "primaryAppUserObjects")
    private List<Class<? extends AppUserOwnedObject>> entityTypes;

    public static class EntitySetting {
        private String type;
        private boolean showActive;
        private boolean showArchived;
        private String defaultSortField;
        private boolean defaultSortAscending;

        public EntitySetting(final String type, final boolean showActive, final boolean showArchived, final String defaultSortField, final boolean defaultSortAscending) {
            this.type = type;
            this.showActive = showActive;
            this.showArchived = showArchived;
            this.defaultSortAscending = defaultSortAscending;
            this.defaultSortField = defaultSortField;
        }

        public String getType() {
            return type;
        }

        public boolean isShowActive() {
            return showActive;
        }

        public boolean isShowArchived() {
            return showArchived;
        }

        public String getDefaultSortField() {
            return defaultSortField;
        }

        public boolean isDefaultSortAscending() {
            return defaultSortAscending;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setShowActive(boolean showActive) {
            this.showActive = showActive;
        }

        public void setShowArchived(boolean showArchived) {
            this.showArchived = showArchived;
        }

        public void setDefaultSortField(String defaultSortField) {
            this.defaultSortField = defaultSortField;
        }

        public void setDefaultSortAscending(boolean defaultSortAscending) {
            this.defaultSortAscending = defaultSortAscending;
        }
    }

    @PostConstruct
    public void postConstruct() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setSizeFull();

        BeanItemContainer<EntitySetting> beanContainer = new BeanItemContainer(EntitySetting.class);
        for (Class<? extends AppUserOwnedObject> entityType : entityTypes) {
            IdObjectEntitySettings settings = entityType.getAnnotation(IdObjectEntitySettings.class);
            beanContainer.addBean(new EntitySetting(settings.plural(), true, false, settings.defaultSortField(), settings.defaultSortAscending()));
        }


        Table preferencesTable = new Table();
        preferencesTable.setContainerDataSource(beanContainer);
        preferencesTable.setVisibleColumns(new String[]{"type", "showActive", "showArchived", "defaultSortField", "defaultSortAscending"});
        preferencesTable.setEditable(true);

        preferencesTable.setSizeFull();
        layout.addComponent(preferencesTable);

        setSizeFull();
        setCompositionRoot(layout);
    }
}
