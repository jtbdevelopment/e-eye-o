package com.jtbdevelopment.e_eye_o.entities;

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

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public AppUser setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public AppUser setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public AppUser setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @Override
    public DateTime getLastLogin() {
        return lastLogin;
    }

    @Override
    public AppUser setLastLogin(final DateTime lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }
}
