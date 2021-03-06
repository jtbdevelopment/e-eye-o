package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.joda.time.DateTime;

/**
 * Date: 3/9/13
 * Time: 11:45 AM
 */
public class AppUserBuilderImpl extends IdObjectBuilderImpl<AppUser> implements AppUserBuilder {
    public AppUserBuilderImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public AppUserBuilder withFirstName(final String firstName) {
        entity.setFirstName(firstName);
        return this;
    }

    @Override
    public AppUserBuilder withLastName(final String lastName) {
        entity.setLastName(lastName);
        return this;
    }

    @Override
    public AppUserBuilder withEmailAddress(final String emailAddress) {
        entity.setEmailAddress(emailAddress);
        return this;
    }

    @Override
    public AppUserBuilder withPassword(final String password) {
        entity.setPassword(password);
        return this;
    }

    @Override
    public AppUserBuilder withLastLogout(final DateTime lastLogin) {
        entity.setLastLogout(lastLogin);
        return this;
    }

    @Override
    public AppUserBuilder withActivated(final boolean activated) {
        entity.setActivated(activated);
        return this;
    }

    @Override
    public AppUserBuilder withActive(final boolean active) {
        entity.setActive(active);
        return this;
    }

    @Override
    public AppUserBuilder withAdmin(final boolean admin) {
        entity.setAdmin(admin);
        return this;
    }
}
