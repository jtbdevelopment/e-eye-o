package com.jtbdevelopment.e_eye_o.ria.vaadin.components.usersettings;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.eventbus.EventBus;
import com.google.common.primitives.Ints;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.TabComponent;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.GeneratedObservationTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectFilterableDisplay;
import com.jtbdevelopment.e_eye_o.ria.vaadin.components.filterabletables.IdObjectTable;
import com.jtbdevelopment.e_eye_o.ria.vaadin.utils.ComponentUtils;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Runo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 6/11/13
 * Time: 6:42 AM
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class UserPreferences extends CustomComponent {
    @Resource(name = "primaryAppUserObjects")
    private List<Class<? extends AppUserOwnedObject>> entityTypes;
    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private IdObjectReflectionHelper reflectionHelper;

    @Autowired
    private EventBus eventBus;

    private static class SettingData<T> {
        private String setting;
        private T defaultValue;

        private SettingData(final String setting, final T defaultValue) {
            this.setting = setting;
            this.defaultValue = defaultValue;
        }
    }

    private List<AbstractField> optionFields = new LinkedList<>();

    @PostConstruct
    public void postConstruct() {
        Panel panel = new Panel();
        panel.addStyleName(Runo.PANEL_LIGHT);
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        GridLayout gridLayout = new GridLayout(2, entityTypes.size() + 1);
        gridLayout.setSpacing(true);
        layout.addComponent(gridLayout);

        gridLayout.addComponent(new Label("Default View On Login:"));
        OptionGroup optionGroup = new OptionGroup(null, Collections2.transform(entityTypes, new Function<Class<? extends AppUserOwnedObject>, String>() {
            @Nullable
            @Override
            public String apply(@Nullable Class<? extends AppUserOwnedObject> input) {
                return input == null ? null : input.getAnnotation(IdObjectEntitySettings.class).plural();
            }
        }));
        optionGroup.setData(new SettingData<>(TabComponent.WEB_VIEW_DEFAULT_TAB_SETTING, Observation.class.getAnnotation(IdObjectEntitySettings.class).plural()));
        optionGroup.addStyleName("horizontal");
        optionFields.add(optionGroup);
        gridLayout.addComponent(optionGroup);

        for (Class<? extends AppUserOwnedObject> entityType : entityTypes) {
            IdObjectEntitySettings entitySettings = entityType.getAnnotation(IdObjectEntitySettings.class);
            gridLayout.addComponent(new Label("Defaults for " + entitySettings.plural() + ":"));

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setSpacing(true);
            gridLayout.addComponent(horizontalLayout);

            String baseConfig = IdObjectFilterableDisplay.WEB_VIEW_BASE_SETTING + entitySettings.plural();
            horizontalLayout.addComponent(new Label("How Many To Show?"));
            ComboBox select = new ComboBox(null, Ints.asList(entitySettings.pageSizes()));
            select.setNullSelectionAllowed(false);
            select.setData(new SettingData<>(baseConfig + IdObjectFilterableDisplay.DEFAULT_SIZE_SETTING, entitySettings.defaultPageSize()));
            select.setWidth(5, Unit.EM);
            select.addStyleName("right-align");
            optionFields.add(select);
            horizontalLayout.addComponent(select);
            CheckBox cb = new CheckBox("Show Active?");
            cb.setData(new SettingData<>(baseConfig + IdObjectFilterableDisplay.SHOW_ACTIVE_SETTING, true));
            optionFields.add(cb);
            horizontalLayout.addComponent(cb);
            cb = new CheckBox("Show Archived?");
            cb.setData(new SettingData<>(baseConfig + IdObjectFilterableDisplay.SHOW_ARCHIVED_SETTING, false));
            optionFields.add(cb);
            horizontalLayout.addComponent(cb);
            horizontalLayout.addComponent(new Label("Default Sort On:"));
            final Map<String, IdObjectFieldSettings> fieldSettings = reflectionHelper.getAllFieldPreferences(entityType);
            select = new ComboBox(null);
            select.setNullSelectionAllowed(false);
            for (final String field : entitySettings.viewFieldOrder()) {
                if (fieldSettings.containsKey(field)) {
                    select.addItem(field);
                    select.setItemCaption(field, fieldSettings.get(field).label());
                }
            }
            select.setData(new SettingData<>(baseConfig + IdObjectTable.DEFAULT_SORT_FIELD_SETTING, entitySettings.defaultSortField()));
            optionFields.add(select);
            horizontalLayout.addComponent(select);

            cb = new CheckBox("Ascending?");
            cb.setData(new SettingData<>(baseConfig + IdObjectTable.DEFAULT_SORT_ASCENDING_SETTING, entitySettings.defaultSortAscending()));
            optionFields.add(cb);
            horizontalLayout.addComponent(cb);
            if (Observation.class.equals(entityType)) {
                cb = new CheckBox("Significant Only?");
                cb.setData(new SettingData<>(baseConfig + GeneratedObservationTable.SIGNIFICANTONLY_DEFAULT, GeneratedObservationTable.DEFAULT_SIGNIFICANT_ONLY));
                optionFields.add(cb);
                horizontalLayout.addComponent(cb);

                horizontalLayout.addComponent(new Label("Observations How Far Back (months)?"));
                select = new ComboBox(null);
                select.setWidth(5, Unit.EM);
                select.addStyleName("right-align");
                select.setPageLength(15);
                select.setNullSelectionAllowed(false);
                for (int i = 0; i <= 12; ++i) {
                    select.addItem(i);
                }
                select.setData(new SettingData<>(baseConfig + GeneratedObservationTable.MONTHSBACK_DEFAULT, GeneratedObservationTable.DEFAULT_FROM_MONTHS_BACK));
                optionFields.add(select);
                horizontalLayout.addComponent(select);
            }
        }

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        Button button = new Button("Save Settings");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateSettings();
            }
        });
        horizontalLayout.addComponent(button);
        button = new Button("Reset");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetToCurrentValues();
            }
        });
        horizontalLayout.addComponent(button);
        layout.addComponent(horizontalLayout);


        panel.setSizeFull();
        panel.setContent(layout);
        setSizeFull();
        setCompositionRoot(panel);
        ComponentUtils.setImmediateForAll(this, true);
    }

    @Override
    public void attach() {
        super.attach();
        resetToCurrentValues();
    }

    @SuppressWarnings("unchecked")
    private void resetToCurrentValues() {
        AppUserSettings fresh = readWriteDAO.get(AppUserSettings.class, getSession().getAttribute(AppUserSettings.class).getId());
        for (AbstractField field : optionFields) {
            SettingData settingData = (SettingData) field.getData();
            if (settingData.defaultValue instanceof String) {
                field.setValue(fresh.getSettingAsString(settingData.setting, (String) settingData.defaultValue));
            }
            if (settingData.defaultValue instanceof Boolean) {
                field.setValue(fresh.getSettingAsBoolean(settingData.setting, (Boolean) settingData.defaultValue));
            }
            if (settingData.defaultValue instanceof Integer) {
                field.setValue(fresh.getSettingAsInt(settingData.setting, (Integer) settingData.defaultValue));
            }
        }
    }

    private void updateSettings() {
        Map<String, Object> settings = new HashMap<>();
        for (AbstractField field : optionFields) {
            SettingData settingData = (SettingData) field.getData();
            settings.put(settingData.setting, field.getValue());
        }
        readWriteDAO.updateSettings(getSession().getAttribute(AppUser.class), settings);
        Notification.show("Preferences saved.", Notification.Type.HUMANIZED_MESSAGE);
    }
}
