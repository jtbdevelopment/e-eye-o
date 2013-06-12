package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserSettingsBuilder;

import java.util.Map;

/**
 * Date: 6/9/13
 * Time: 7:10 PM
 */
public class AppUserSettingsBuilderImpl2 extends AppUserOwnedObjectBuilderImpl<AppUserSettings> implements AppUserSettingsBuilder {
    public AppUserSettingsBuilderImpl2(final AppUserSettings entity) {
        super(entity);
    }

    @Override
    public AppUserSettingsBuilderImpl2 withSetting(final String name, final Object value) {
        entity.setSetting(name, value);
        return this;
    }

    @Override
    public AppUserSettingsBuilderImpl2 withSettings(final Map<String, String> settings) {
        entity.setSettings(settings);
        return this;
    }
}
