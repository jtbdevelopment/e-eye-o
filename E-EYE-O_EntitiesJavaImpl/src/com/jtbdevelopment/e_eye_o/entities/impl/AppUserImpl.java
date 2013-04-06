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
    private boolean active = true;
    private boolean admin = false;
    private boolean activated = false;
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

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public void setActivated(final boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    @Override
    public void setAdmin(final boolean admin) {
        this.admin = admin;
    }
}
