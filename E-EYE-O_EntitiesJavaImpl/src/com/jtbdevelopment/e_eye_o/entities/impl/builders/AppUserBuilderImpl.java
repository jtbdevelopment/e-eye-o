package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.builders.AppUserBuilder;
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
}
