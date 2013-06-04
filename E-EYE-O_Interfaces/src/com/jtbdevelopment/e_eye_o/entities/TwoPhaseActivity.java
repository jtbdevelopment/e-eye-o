package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectPreferredDescription;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectTableDisplayPreferences;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

/**
 * Date: 4/6/13
 * Time: 2:42 PM
 */
@IdObjectPreferredDescription(singular = "Two Phase Operations", plural = "Two Phase Operations")
@IdObjectTableDisplayPreferences(displayable = false, defaultSortField = "expirationTime")
public interface TwoPhaseActivity extends AppUserOwnedObject {
    public static final String ACTIVITY_TYPE_CANNOT_BE_NULL = "TwoPhaseActivity.activityType" + CANNOT_BE_NULL_ERROR;
    public static final String EXPIRATION_TIME_CANNOT_BE_NULL = "TwoPhaseActivity.expirationTime" + CANNOT_BE_NULL_ERROR;

    public enum Activity {
        ACCOUNT_ACTIVATION,
        PASSWORD_RESET
    }

    @NotNull(message = ACTIVITY_TYPE_CANNOT_BE_NULL)
    Activity getActivityType();

    void setActivityType(final Activity activityType);

    @NotNull(message = EXPIRATION_TIME_CANNOT_BE_NULL)
    DateTime getExpirationTime();

    void setExpirationTime(final DateTime expirationTime);
}
