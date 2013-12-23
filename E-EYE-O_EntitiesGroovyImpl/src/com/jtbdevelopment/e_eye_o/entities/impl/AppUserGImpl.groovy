package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.AppUser
import groovy.transform.CompileStatic
import org.joda.time.DateTime

/**
 * Date: 11/21/13
 * Time: 7:07 AM
 */
@CompileStatic
class AppUserGImpl extends IdObjectGImpl implements AppUser {
    String firstName = ""
    String lastName = ""
    String emailAddress = ""
    String password = ""
    DateTime lastLogout = NEVER_LOGGED_IN
    boolean activated = false
    boolean active = true
    boolean admin = false

    @Override
    String getSummaryDescription() {
        return (firstName.trim() + " " + lastName.trim()).trim()
    }

}
