package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.joda.time.DateTime;

/**
 * Date: 3/9/13
 * Time: 11:13 AM
 */
public interface AppUserBuilder extends IdObjectBuilder<AppUser> {
    AppUserBuilder withFirstName(final String firstName);

    AppUserBuilder withLastName(final String lastName);

    AppUserBuilder withEmailAddress(final String emailAddress);

    AppUserBuilder withPassword(final String password);

    AppUserBuilder withLastLogout(final DateTime lastLogin);

    AppUserBuilder withActivated(final boolean activated);

    AppUserBuilder withActive(final boolean active);

    AppUserBuilder withAdmin(final boolean admin);
}
