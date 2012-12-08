package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;

/**
 * Date: 11/25/12
 * Time: 3:12 PM
 */
public interface AppUser extends IdObject {
    DateTime NEVER_LOGGED_IN = new DateTime(2000, 1, 1, 0, 0);

    String getLogin();

    AppUser setLogin(String login);

    String getFirstName();

    AppUser setFirstName(String firstName);

    String getLastName();

    AppUser setLastName(String lastName);

    String getEmailAddress();

    AppUser setEmailAddress(String emailAddress);

    DateTime getLastLogin();

    AppUser setLastLogin(DateTime lastLogin);
}
