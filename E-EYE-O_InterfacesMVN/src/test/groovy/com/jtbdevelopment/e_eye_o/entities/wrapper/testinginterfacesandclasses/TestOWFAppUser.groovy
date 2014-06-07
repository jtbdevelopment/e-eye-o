package com.jtbdevelopment.e_eye_o.entities.wrapper.testinginterfacesandclasses

import com.jtbdevelopment.e_eye_o.entities.AppUser
import org.joda.time.DateTime

/**
 * Date: 12/8/13
 * Time: 3:57 PM
 */
public class TestOWFAppUser implements AppUser {
    String firstName
    String lastName
    boolean activated
    boolean admin
    boolean active
    String emailAddress
    String password
    String id
    DateTime modificationTimestamp
    DateTime lastLogout
    String summaryDescription
}
