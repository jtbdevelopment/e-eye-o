package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.superclasses.IdObject;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.security.InvalidParameterException;

/**
 * Date: 11/19/12
 * Time: 10:17 PM
 */
@Entity
public class AppUser extends IdObject {
    private String login;
    //  TODO
    @Transient
    private String password = "";
    private String firstName = "";
    private String lastName = "";
    private String emailAddress = "";
    private DateTime lastLogin;

    @Column(nullable = false, unique = true)
    public String getLogin() {
        return login;
    }

    public AppUser setLogin(final String login) {
        if ( login == null ) {
            throw new InvalidParameterException("login cannot be null");
        }
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AppUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public AppUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public AppUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress == null ? "" :emailAddress;
    }

    public AppUser setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public DateTime getLastLogin() {
        return lastLogin;
    }

    public AppUser setLastLogin(DateTime lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }
}
