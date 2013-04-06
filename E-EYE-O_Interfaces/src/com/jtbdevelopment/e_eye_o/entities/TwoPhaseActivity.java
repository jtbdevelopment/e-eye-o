package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;

/**
 * Date: 4/6/13
 * Time: 2:42 PM
 */
public interface TwoPhaseActivity extends AppUserOwnedObject {
    public enum Activity {
        ACCOUNT_ACTIVATION,
        PASSWORD_RESET
    }

    Activity getActivityType();

    void setActivityType(final Activity activityType);

    DateTime getExpirationTime();

    void setExpirationTime(final DateTime expirationTime);
}
