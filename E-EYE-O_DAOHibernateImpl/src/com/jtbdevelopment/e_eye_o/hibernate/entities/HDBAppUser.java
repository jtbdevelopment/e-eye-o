package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserImpl;
import org.hibernate.annotations.Type;
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
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
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
