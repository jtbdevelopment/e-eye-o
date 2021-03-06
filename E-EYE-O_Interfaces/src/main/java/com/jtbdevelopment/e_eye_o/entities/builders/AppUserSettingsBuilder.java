package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;

/**
 * Date: 6/9/13
 * Time: 7:01 PM
 */
public interface AppUserSettingsBuilder extends AppUserOwnedObjectBuilder<AppUserSettings> {
    AppUserSettingsBuilder withSetting(final String name, final Object value);
}
