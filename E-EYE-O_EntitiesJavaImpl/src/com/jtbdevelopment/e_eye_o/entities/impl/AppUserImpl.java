package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.joda.time.DateTime;

/**
 * Date: 11/28/12
 * Time: 9:08 PM
 */
public class AppUserImpl extends IdObjectImpl implements AppUser {
    private String firstName = "";
    private String lastName = "";
    private String emailAddress = "";
    private DateTime lastLogin = NEVER_LOGGED_IN;

    AppUserImpl() {
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public DateTime getLastLogin() {
        return lastLogin;
    }

    @Override
    public void setLastLogin(final DateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String getViewableDescription() {
        return (firstName + " " + lastName).trim();
    }
}
