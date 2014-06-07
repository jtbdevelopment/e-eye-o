package com.jtbdevelopment.e_eye_o.reflection.testinterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import org.joda.time.DateTime

/**
 * Date: 12/8/13
 * Time: 9:17 PM
 */
public class TestIOR1GImpl implements TestIORInterface1 {
    String id
    DateTime modificationTimestamp
    int intValue;
    boolean booleanValue;
    Set<String> stringValues = [] as Set;
    Object objectValue;
    AppUserOwnedObject appUserOwnedObject;

    @Override
    public String getFirstStringValue() {
        if (stringValues.isEmpty())
            return null;
        else
            return stringValues.iterator().next();
    }

    @Override
    String getSummaryDescription() {
        return id
    }
}
