package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings;

import java.beans.Transient;
import java.util.Map;

/**
 * Date: 6/9/13
 * Time: 4:43 PM
 */
@IdObjectEntitySettings(singular = "Setting", plural = "Settings", viewable = false, editable = false, defaultSortField = "")
public interface AppUserSettings extends AppUserOwnedObject {
    @IdObjectFieldSettings(viewable = false, editableBy = IdObjectFieldSettings.EditableBy.CONTROLLED, label = "Settings")
    Map<String, String> getSettings();

    void setSettings(final Map<String, String> settings);

    /**
     * Internally will store value as string via toString
     *
     * @param name  preference name
     * @param value
     */
    void setSetting(final String name, final Object value);

    @Transient
    String getSettingAsString(final String name, final String defaultValue);

    @Transient
    int getSettingAsInt(final String name, final int defaultValue);

    @Transient
    boolean getSettingAsBoolean(final String name, final boolean defaultValue);

}
