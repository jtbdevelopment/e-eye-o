package com.jtbdevelopment.e_eye_o.entities;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:12 PM
 */
public interface AppUser extends IdObject {
    public final static DateTime NEVER_LOGGED_IN = new DateTime(2000, 1, 1, 0, 0);

    public final static int MAX_EMAIL_SIZE = 40;
    public final static String LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR = "AppUser.login" + CANNOT_BE_NULL_ERROR;
    public final static String EMAIL_MUST_BE_A_VALID_FORMAT_ERROR = "AppUser.email must be a valid format.";
    public final static String EMAIL_CANNOT_BE_NULL_ERROR = "AppUser.email" + CANNOT_BE_NULL_ERROR;
    public final static String LAST_NAME_CANNOT_BE_NULL_ERROR = "AppUser.lastName" + CANNOT_BE_NULL_ERROR + ".";
    public final static String FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR = "AppUser.firstName" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String APP_USER_EMAIL_SIZE_ERROR = "AppUser.email cannot be longer than " + MAX_EMAIL_SIZE + " characters.";
    public final static String APP_USER_FIRST_NAME_SIZE_ERROR = "AppUser.firstName" + NAME_SIZE_ERROR;
    public final static String APP_USER_LAST_NAME_SIZE_ERROR = "AppUser.lastName" + NAME_SIZE_ERROR;

    @NotEmpty(message = FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_NAME_SIZE, message = APP_USER_FIRST_NAME_SIZE_ERROR)
    String getFirstName();

    void setFirstName(final String firstName);

    @NotNull(message = LAST_NAME_CANNOT_BE_NULL_ERROR)
    @Size(max = MAX_NAME_SIZE, message = APP_USER_LAST_NAME_SIZE_ERROR)
    String getLastName();

    void setLastName(final String lastName);

    @NotNull(message = EMAIL_CANNOT_BE_NULL_ERROR)
    @Email(message = EMAIL_MUST_BE_A_VALID_FORMAT_ERROR)
    @Size(max = MAX_EMAIL_SIZE, message = APP_USER_EMAIL_SIZE_ERROR)
    String getEmailAddress();

    void setEmailAddress(final String emailAddress);

    @NotNull(message = LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR)
    DateTime getLastLogout();

    void setLastLogout(final DateTime lastLogout);
}
