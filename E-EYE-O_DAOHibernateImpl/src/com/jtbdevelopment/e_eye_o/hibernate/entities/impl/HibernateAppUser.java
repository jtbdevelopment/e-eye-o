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
    @SuppressWarnings("unused")  //  Hibernate
    protected HibernateAppUser() {
    }

    @SuppressWarnings("unused")  //  HibernateIdObjectWrapperFactory via reflection
    protected HibernateAppUser(final AppUser appUser) {
        super(appUser);
    }

    @Override
    @Column(nullable = false, length = MAX_NAME_SIZE)
    public String getFirstName() {
        return wrapped.getFirstName();
    }

    @Override
    public AppUser setFirstName(final String firstName) {
        wrapped.setFirstName(firstName);
        return this;
    }

    @Override
    @Column(length = MAX_NAME_SIZE)
    public String getLastName() {
        return wrapped.getLastName();
    }

    @Override
    public AppUser setLastName(final String lastName) {
        wrapped.setLastName(lastName);
        return this;
    }

    @Override
    @Column(nullable = false, length = MAX_EMAIL_SIZE, unique = true)
    public String getEmailAddress() {
        return wrapped.getEmailAddress();
    }

    @Override
    public AppUser setEmailAddress(final String emailAddress) {
        wrapped.setEmailAddress(emailAddress);
        return this;
    }

    @Override
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(nullable = false)
    public DateTime getLastLogin() {
        return wrapped.getLastLogin();
    }

    @Override
    public AppUser setLastLogin(final DateTime lastLogin) {
        wrapped.setLastLogin(lastLogin);
        return this;
    }
}