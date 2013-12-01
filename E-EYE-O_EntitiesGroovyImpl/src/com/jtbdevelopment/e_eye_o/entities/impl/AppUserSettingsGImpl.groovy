package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings

/**
 * Date: 11/30/13
 * Time: 12:27 PM
 */
class AppUserSettingsGImpl extends AppUserOwnedObjectGImpl implements AppUserSettings {
    final Map<String, String> settings = [:]

    @Override
    public Map<String, String> getSettings() {
        return settings.asImmutable()
    }

    @Override
    public void updateSettings(final Map<String, Object> settings) {
        settings.each { this.settings.put(it.key, it.value.toString()) }
    }

    @Override
    void setSetting(final String name, final Object value) {
        this.settings.put(name, value.toString())
    }

    @Override
    String getSettingAsString(final String name, final String defaultValue) {
        if (settings.containsKey(name)) {
            return settings[(name)]
        }
        return defaultValue
    }

    @Override
    int getSettingAsInt(final String name, final int defaultValue) {
        if (settings.containsKey(name)) {
            return settings[(name)].toInteger()
        }
        return defaultValue
    }

    @Override
    boolean getSettingAsBoolean(final String name, final boolean defaultValue) {
        if (settings.containsKey(name)) {
            return settings[(name)].toBoolean()
        }
        return defaultValue
    }

}
