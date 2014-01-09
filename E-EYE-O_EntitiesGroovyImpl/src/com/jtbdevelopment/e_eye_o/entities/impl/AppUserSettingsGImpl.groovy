package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings
import groovy.transform.CompileStatic

/**
 * Date: 11/30/13
 * Time: 12:27 PM
 */
@CompileStatic
class AppUserSettingsGImpl extends AppUserOwnedObjectGImpl implements AppUserSettings {
    Map<String, String> settingsMap = [:]

    @Override
    public Map<String, String> getSettings() {
        return new HashMap<String, String>(settingsMap)
    }

    @Override
    public void setSettings(final Map<String, Object> settings) {
        this.settingsMap.clear()
        updateSettings(settings)
    }

    @Override
    public void updateSettings(final Map<String, Object> s) {
        s.each { String key, Object value -> this.settingsMap += [(key): value.toString()] }
    }

    @Override
    void setSetting(final String name, final Object value) {
        this.settingsMap.put(name, value.toString())
    }

    @Override
    String getSettingAsString(final String name, final String defaultValue) {
        if (settingsMap.containsKey(name)) {
            return settingsMap[(name)]
        }
        return defaultValue
    }

    @Override
    int getSettingAsInt(final String name, final int defaultValue) {
        if (settingsMap.containsKey(name)) {
            return settingsMap[(name)].toInteger()
        }
        return defaultValue
    }

    @Override
    long getSettingAsLong(final String name, final long defaultValue) {
        if (settings.containsKey(name)) {
            return settingsMap[(name)].toLong()
        }
        return defaultValue
    }

    @Override
    boolean getSettingAsBoolean(final String name, final boolean defaultValue) {
        if (settingsMap.containsKey(name)) {
            return settingsMap[(name)].toBoolean()
        }
        return defaultValue
    }

}
