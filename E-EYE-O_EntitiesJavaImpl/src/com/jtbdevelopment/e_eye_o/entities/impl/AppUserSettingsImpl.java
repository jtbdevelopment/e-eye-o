package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 6/9/13
 * Time: 7:05 PM
 */
public class AppUserSettingsImpl extends AppUserOwnedObjectImpl implements AppUserSettings {
    private Map<String, String> settings = new HashMap<>();

    public AppUserSettingsImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public Map<String, String> getSettings() {
        return new HashMap<>(settings);
    }

    @Override
    public void updateSettings(final Map<String, Object> settings) {
        for (Map.Entry<String, Object> setting : settings.entrySet()) {
            this.settings.put(setting.getKey(), setting.getValue().toString());
        }
    }

    @Override
    public void setSetting(final String name, final Object value) {
        settings.put(name, value.toString());
    }

    @Override
    public String getSettingAsString(final String name, final String defaultValue) {
        if (settings.containsKey(name)) {
            return settings.get(name);
        }
        return defaultValue;
    }

    @Override
    public int getSettingAsInt(String name, int defaultValue) {
        if (settings.containsKey(name)) {
            return Integer.parseInt(settings.get(name));
        }
        return defaultValue;
    }

    @Override
    public boolean getSettingAsBoolean(String name, boolean defaultValue) {
        if (settings.containsKey(name)) {
            return Boolean.parseBoolean(settings.get(name));
        }
        return defaultValue;
    }
}
