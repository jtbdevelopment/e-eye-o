package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserImpl;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/19/12
 * Time: 10:17 PM
 */
@Entity(name = "AppUser")
public class HDBAppUser extends HDBIdObject<AppUser> implements AppUser {
    protected HDBAppUser() {
        super(new AppUserImpl());
    }

    public HDBAppUser(final AppUser appUser) {
        super(appUser);
    }

    @Column(nullable = false, unique = true)
    public String getLogin() {
        return wrapped.getLogin();
    }

    @Override
    public AppUser setLogin(final String login) {
        wrapped.setLogin(login);
        return this;
    }

    @Override
    public String getFirstName() {
        return wrapped.getFirstName();
    }

    @Override
    public AppUser setFirstName(final String firstName) {
        wrapped.setFirstName(firstName);
        return this;
    }

    @Override
    public String getLastName() {
        return wrapped.getLastName();
    }

    @Override
    public AppUser setLastName(final String lastName) {
        wrapped.setLastName(lastName);
        return this;
    }

    @Override
    public String getEmailAddress() {
        return wrapped.getEmailAddress();
    }

    @Override
    public AppUser setEmailAddress(final String emailAddress) {
        wrapped.setEmailAddress(emailAddress);
        return this;
    }

    @Override
    public DateTime getLastLogin() {
        return wrapped.getLastLogin();
    }

    @Override
    public AppUser setLastLogin(final DateTime lastLogin) {
        wrapped.setLastLogin(lastLogin);
        return this;
    }
}
