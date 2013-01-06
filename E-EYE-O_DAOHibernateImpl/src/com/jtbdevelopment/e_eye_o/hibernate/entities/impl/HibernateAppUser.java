package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/19/12
 * Time: 10:17 PM
 */
@Entity(name = "AppUser")
public class HibernateAppUser extends HibernateIdObject<AppUser> implements AppUser {
    protected HibernateAppUser() {
        super();
    }

    public HibernateAppUser(final AppUser appUser) {
        super(appUser);
    }

    @Override
    @Column(nullable = false, length = MAX_NAME_SIZE)
    public String getFirstName() {
        return getWrapped().getFirstName();
    }

    @Override
    public AppUser setFirstName(final String firstName) {
        getWrapped().setFirstName(firstName);
        return this;
    }

    @Override
    @Column(length = MAX_NAME_SIZE)
    public String getLastName() {
        return getWrapped().getLastName();
    }

    @Override
    public AppUser setLastName(final String lastName) {
        getWrapped().setLastName(lastName);
        return this;
    }

    @Override
    @Column(nullable = false, length = MAX_EMAIL_SIZE, unique = true)
    public String getEmailAddress() {
        return getWrapped().getEmailAddress();
    }

    @Override
    public AppUser setEmailAddress(final String emailAddress) {
        getWrapped().setEmailAddress(emailAddress);
        return this;
    }

    @Override
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(nullable = false)
    public DateTime getLastLogin() {
        return getWrapped().getLastLogin();
    }

    @Override
    public AppUser setLastLogin(final DateTime lastLogin) {
        getWrapped().setLastLogin(lastLogin);
        return this;
    }
}
