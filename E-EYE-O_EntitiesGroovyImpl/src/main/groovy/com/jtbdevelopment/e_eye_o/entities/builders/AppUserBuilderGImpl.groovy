package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.AppUser
import org.joda.time.DateTime

/**
 * Date: 12/1/13
 * Time: 3:40 PM
 */
class AppUserBuilderGImpl extends IdObjectBuilderGImpl<AppUser> implements AppUserBuilder {
    @Override
    AppUserBuilder withFirstName(final String firstName) {
        entity.firstName = firstName
        return this
    }

    @Override
    AppUserBuilder withLastName(final String lastName) {
        entity.lastName = lastName
        return this
    }

    @Override
    AppUserBuilder withEmailAddress(final String emailAddress) {
        entity.emailAddress = emailAddress
        return this
    }

    @Override
    AppUserBuilder withPassword(final String password) {
        entity.password = password
        return this
    }

    @Override
    AppUserBuilder withLastLogout(final DateTime lastLogin) {
        entity.lastLogout = lastLogin
        return this
    }

    @Override
    AppUserBuilder withActivated(final boolean activated) {
        entity.activated = activated
        return this
    }

    @Override
    AppUserBuilder withActive(final boolean active) {
        entity.active = active
        return this
    }

    @Override
    AppUserBuilder withAdmin(final boolean admin) {
        entity.admin = admin
        return this
    }
}
