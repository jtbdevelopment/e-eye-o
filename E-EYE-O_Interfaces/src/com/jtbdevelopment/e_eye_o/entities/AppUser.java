package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Date: 11/25/12
 * Time: 3:12 PM
 */
@PreferredDescription(singular = "User", plural = "Users")
public interface AppUser extends IdObject {
    public final static DateTime NEVER_LOGGED_IN = UNINITIALISED_LOCAL_DATE_TIME.toDateTime();

    public final static int MAX_EMAIL_SIZE = 40;
    public final static int MAX_PASSWORD_SIZE = 200;
    public final static String LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR = "AppUser.login" + CANNOT_BE_NULL_ERROR;
    public final static String EMAIL_MUST_BE_A_VALID_FORMAT_ERROR = "AppUser.email must be a valid email format.";
    public final static String EMAIL_CANNOT_BE_NULL_ERROR = "AppUser.email" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String LAST_NAME_CANNOT_BE_NULL_ERROR = "AppUser.lastName" + CANNOT_BE_NULL_ERROR + ".";
    public final static String FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR = "AppUser.firstName" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String APP_USER_EMAIL_SIZE_ERROR = "AppUser.email cannot be longer than " + MAX_EMAIL_SIZE + " characters.";
    public final static String APP_USER_FIRST_NAME_SIZE_ERROR = "AppUser.firstName" + NAME_SIZE_ERROR;
    public final static String APP_USER_LAST_NAME_SIZE_ERROR = "AppUser.lastName" + NAME_SIZE_ERROR;
    public final static String APP_PASSWORD_SIZE_ERROR = "AppUser.password" + NAME_SIZE_ERROR;
    public final static String APP_PASSWORD_CANNOT_BE_NULL_ERROR = "AppUser.password" + CANNOT_BE_BLANK_OR_NULL_ERROR;
    public final static String ACTIVATED_CANNOT_BE_NULL_ERROR = "AppUser.activated" + CANNOT_BE_NULL_ERROR;
    public final static String ACTIVE_CANNOT_BE_NULL_ERROR = "AppUser.active" + CANNOT_BE_NULL_ERROR;
    public final static String ADMIN_CANNOT_BE_NULL_ERROR = "AppUser.admin" + CANNOT_BE_NULL_ERROR;

    @NotEmpty(message = FIRST_NAME_CANNOT_BE_BLANK_OR_NULL_ERROR)
    @Size(max = MAX_NAME_SIZE, message = APP_USER_FIRST_NAME_SIZE_ERROR)
    String getFirstName();

    void setFirstName(final String firstName);

    @NotNull(message = LAST_NAME_CANNOT_BE_NULL_ERROR)
    @Size(max = MAX_NAME_SIZE, message = APP_USER_LAST_NAME_SIZE_ERROR)
    String getLastName();

    void setLastName(final String lastName);

    @NotEmpty(message = EMAIL_CANNOT_BE_NULL_ERROR)
    @Email(message = EMAIL_MUST_BE_A_VALID_FORMAT_ERROR)
    @Size(max = MAX_EMAIL_SIZE, message = APP_USER_EMAIL_SIZE_ERROR)
    String getEmailAddress();

    void setEmailAddress(final String emailAddress);

    @NotEmpty(message = APP_PASSWORD_CANNOT_BE_NULL_ERROR)
    @Size(max = MAX_PASSWORD_SIZE, message = APP_PASSWORD_SIZE_ERROR)
    String getPassword();

    void setPassword(final String password);

    @NotNull(message = LAST_LOGIN_TIME_CANNOT_BE_NULL_ERROR)
    DateTime getLastLogout();

    void setLastLogout(final DateTime lastLogout);

    @NotNull(message = ACTIVATED_CANNOT_BE_NULL_ERROR)
    boolean isActivated();

    void setActivated(final boolean activated);

    @NotNull(message = ACTIVE_CANNOT_BE_NULL_ERROR)
    boolean isActive();

    void setActive(final boolean active);

    @NotNull(message = ADMIN_CANNOT_BE_NULL_ERROR)
    boolean isAdmin();

    void setAdmin(final boolean admin);

}

