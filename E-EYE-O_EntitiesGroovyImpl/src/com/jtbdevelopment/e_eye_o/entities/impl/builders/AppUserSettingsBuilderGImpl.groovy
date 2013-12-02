package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserSettingsBuilder

/**
 * Date: 12/1/13
 * Time: 3:39 PM
 */
class AppUserSettingsBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<AppUserSettings> implements AppUserSettingsBuilder {
    @Override
    AppUserSettingsBuilder withSetting(final String name, final Object value) {
        entity.setSetting(name, value)
        return this
    }
}
