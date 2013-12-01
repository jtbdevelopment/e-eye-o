package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUserSettings;

import javax.persistence.*;
import java.util.Map;

/**
 * Date: 6/9/13
 * Time: 7:17 PM
 */
@Entity(name = "AppUserSettings")
public class HibernateAppUserSettings extends HibernateAppUserOwnedObject<AppUserSettings> implements AppUserSettings {
    public HibernateAppUserSettings() {
    }

    public HibernateAppUserSettings(AppUserSettings appUserOwnedObject) {
        super(appUserOwnedObject);
    }

    //  Duplicate column to allow a unique constraint at table level
    @Column(nullable = false, unique = true)
    @SuppressWarnings("unused")  // Hibernate
    public String getAppUserID() {
        return wrapped.getAppUser().getId();
    }

    @SuppressWarnings("unused")  // Hibernate
    public void setAppUserID(final String appUserID) {
        //  Do nothing
    }

    @Override
    @ElementCollection
    @MapKeyColumn(name = "setting")
    @Column(name = "value")
    @CollectionTable(name = "AppUserSettingsEntries", joinColumns = @JoinColumn(name = "appUserSettings_id"), uniqueConstraints = @UniqueConstraint(columnNames = {"appUserSettings_id", "setting"}))
    public Map<String, String> getSettings() {
        return wrapped.getSettings();
    }

    @Override
    public void updateSettings(final Map<String, Object> settings) {
        wrapped.updateSettings(settings);
    }

    @Override
    public void setSetting(final String name, final Object value) {
        wrapped.setSetting(name, value);
    }

    @Override
    @Transient
    public String getSettingAsString(final String name, final String defaultValue) {
        return wrapped.getSettingAsString(name, defaultValue);
    }

    @Override
    @Transient
    public int getSettingAsInt(String name, int defaultValue) {
        return wrapped.getSettingAsInt(name, defaultValue);
    }

    @Override
    @Transient
    public boolean getSettingAsBoolean(String name, boolean defaultValue) {
        return wrapped.getSettingAsBoolean(name, defaultValue);
    }
}
