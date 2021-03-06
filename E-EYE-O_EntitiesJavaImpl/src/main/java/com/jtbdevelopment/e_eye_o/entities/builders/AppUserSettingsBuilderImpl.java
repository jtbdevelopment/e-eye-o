package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;

/**
 * Date: 6/9/13
 * Time: 7:10 PM
 */
public class AppUserSettingsBuilderImpl extends AppUserOwnedObjectBuilderImpl<AppUserSettings> implements AppUserSettingsBuilder {
    public AppUserSettingsBuilderImpl(final AppUserSettings entity) {
        super(entity);
    }

    @Override
    public AppUserSettingsBuilderImpl withSetting(final String name, final Object value) {
        entity.setSetting(name, value);
        return this;
    }
}
