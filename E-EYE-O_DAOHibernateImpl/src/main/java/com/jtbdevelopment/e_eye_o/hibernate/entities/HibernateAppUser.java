package com.jtbdevelopment.e_eye_o.hibernate.entities;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 11/19/12
 * Time: 10:17 PM
 */
@Entity(name = "AppUser")
@Audited
@Proxy(lazy = false)
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
    public void setFirstName(final String firstName) {
        wrapped.setFirstName(firstName);
    }

    @Override
    @Column(length = MAX_NAME_SIZE)
    public String getLastName() {
        return wrapped.getLastName();
    }

    @Override
    public void setLastName(final String lastName) {
        wrapped.setLastName(lastName);
    }

    @Override
    @Column(nullable = false, length = MAX_EMAIL_SIZE, unique = true)
    public String getEmailAddress() {
        return wrapped.getEmailAddress();
    }

    @Override
    public void setEmailAddress(final String emailAddress) {
        wrapped.setEmailAddress(emailAddress);
    }

    @Override
    @Column(nullable = false, length = MAX_PASSWORD_SIZE)
    public String getPassword() {
        return wrapped.getPassword();
    }

    @Override
    public void setPassword(final String password) {
        wrapped.setPassword(password);
    }

    @Override
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(nullable = false)
    public DateTime getLastLogout() {
        return wrapped.getLastLogout().withMillisOfSecond(0);
    }

    @Override
    public void setLastLogout(final DateTime lastLogout) {
        wrapped.setLastLogout(lastLogout.withMillisOfSecond(0));
    }

    @Override
    @Column(nullable = false)
    public boolean isActivated() {
        return wrapped.isActivated();
    }

    @Override
    public void setActivated(final boolean activated) {
        wrapped.setActivated(activated);
    }

    @Override
    @Column(nullable = false)
    public boolean isActive() {
        return wrapped.isActive();
    }

    @Override
    public void setActive(final boolean active) {
        wrapped.setActive(active);
    }

    @Override
    @Column(nullable = false)
    public boolean isAdmin() {
        return wrapped.isAdmin();
    }

    @Override
    public void setAdmin(final boolean admin) {
        wrapped.setAdmin(admin);
    }
}
