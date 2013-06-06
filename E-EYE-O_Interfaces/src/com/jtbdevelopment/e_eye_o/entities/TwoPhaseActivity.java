package com.jtbdevelopment.e_eye_o.entities;

import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectDisplayPreferences;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldPreferences;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;

/**
 * Date: 4/6/13
 * Time: 2:42 PM
 */
@IdObjectDisplayPreferences(viewable = false, defaultSortField = "expirationTime", editable = false, singular = "Two Phase Operations", plural = "Two Phase Operations")
public interface TwoPhaseActivity extends AppUserOwnedObject {
    public static final String ACTIVITY_TYPE_CANNOT_BE_NULL = "TwoPhaseActivity.activityType" + CANNOT_BE_NULL_ERROR;
    public static final String EXPIRATION_TIME_CANNOT_BE_NULL = "TwoPhaseActivity.expirationTime" + CANNOT_BE_NULL_ERROR;

    public enum Activity {
        ACCOUNT_ACTIVATION,
        PASSWORD_RESET
    }

    @NotNull(message = ACTIVITY_TYPE_CANNOT_BE_NULL)
    @IdObjectFieldPreferences(editableBy = IdObjectFieldPreferences.EditableBy.NONE, label = "Type")
    Activity getActivityType();

    void setActivityType(final Activity activityType);

    @NotNull(message = EXPIRATION_TIME_CANNOT_BE_NULL)
    @IdObjectFieldPreferences(editableBy = IdObjectFieldPreferences.EditableBy.NONE, label = "Expiry")
    DateTime getExpirationTime();

    void setExpirationTime(final DateTime expirationTime);
}
