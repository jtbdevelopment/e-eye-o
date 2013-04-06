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
    private String password = "";
    private DateTime lastLogout = NEVER_LOGGED_IN;

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
    public DateTime getLastLogout() {
        return lastLogout;
    }

    @Override
    public void setLastLogout(final DateTime lastLogout) {
        this.lastLogout = lastLogout;
    }

    @Override
    public String getSummaryDescription() {
        return (firstName + " " + lastName).trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
