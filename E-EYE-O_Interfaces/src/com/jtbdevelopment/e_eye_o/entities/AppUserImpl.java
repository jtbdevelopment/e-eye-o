package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;

import java.security.InvalidParameterException;

/**
 * Date: 11/28/12
 * Time: 9:08 PM
 */
public class AppUserImpl extends IdObjectImpl implements AppUser {
    private String login = "";
    private String firstName = "";
    private String lastName = "";
    private String emailAddress = "";
    private DateTime lastLogin;

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public AppUser setLogin(final String login) {
        if (login == null) {
            throw new InvalidParameterException("login cannot be null");
        }
        this.login = login;
        return this;
    }

    @Override
    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    @Override
    public AppUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    @Override
    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    @Override
    public AppUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress == null ? "" : emailAddress;
    }

    @Override
    public AppUser setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @Override
    public DateTime getLastLogin() {
        return lastLogin;
    }

    @Override
    public AppUser setLastLogin(DateTime lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }
}
