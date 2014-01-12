package com.jtbdevelopment.e_eye_o.reflection.testinterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings

import java.beans.Transient

/**
 * Date: 12/3/13
 * Time: 9:31 PM
 */
public interface TestIORInterface1 extends IdObject {
    @IdObjectFieldSettings(viewable = true)
    public AppUserOwnedObject getAppUserOwnedObject();

    public void setAppUserOwnedObject(final AppUserOwnedObject appUserOwnedObject);

    @IdObjectFieldSettings(viewable = false)
    public int getIntValue();

    public void setIntValue(final int value);

    public Set<String> getStringValues();

    public void setStringValues(final Set<String> stringValues);

    public boolean isBooleanValue();

    public void setBooleanValue(final boolean boolValue);

    public Object getObjectValue();

    public void setObjectValue(final Object objectValue);

    @Transient
    public String getFirstStringValue();
}
