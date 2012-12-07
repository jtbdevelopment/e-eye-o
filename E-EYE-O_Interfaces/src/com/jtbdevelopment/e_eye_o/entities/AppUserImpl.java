package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;

import java.security.InvalidParameterException;

/**
 * Date: 11/28/12
 * Time: 9:08 PM
 */
public class AppUserImpl extends IdObjectImpl implements AppUser {
    public static final DateTime NEVER_LOGGED_IN = new DateTime(2000, 1, 1, 0, 0);
    private String login = "";
    private String firstName = "";
    private String lastName = "";
    private String emailAddress = "";
    private DateTime lastLogin = NEVER_LOGGED_IN;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public AppUser setLogin(final String login) {
        validateNonEmptyValue(login);
        this.login = login;
        return this;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public AppUser setFirstName(final String firstName) {
        validateNonNullValue(firstName);
        this.firstName = firstName;
        return this;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public AppUser setLastName(final String lastName) {
        validateNonNullValue(lastName);
        this.lastName = lastName;
        return this;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public AppUser setEmailAddress(final String emailAddress) {
        validateNonNullValue(emailAddress);
        this.emailAddress = emailAddress;
        return this;
    }

    @Override
    public DateTime getLastLogin() {
        return lastLogin;
    }

    @Override
    public AppUser setLastLogin(final DateTime lastLogin) {
        validateNonNullValue(lastLogin);
        this.lastLogin = lastLogin;
        return this;
    }
}
