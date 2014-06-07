package com.jtbdevelopment.e_eye_o.reflection.testinterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.AppUser
import org.joda.time.DateTime

/**
 * Date: 12/8/13
 * Time: 9:17 PM
 */
public class TestIOR3GImpl implements TestIORInterface3 {
    AppUser appUser
    boolean archived
    String id
    DateTime modificationTimestamp

    @Override
    String getSummaryDescription() {
        return id
    }
}
