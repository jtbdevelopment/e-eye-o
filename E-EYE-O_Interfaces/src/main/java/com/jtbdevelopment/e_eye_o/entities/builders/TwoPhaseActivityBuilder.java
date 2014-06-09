package com.jtbdevelopment.e_eye_o.entities.builders;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.joda.time.DateTime;

/**
 * Date: 4/6/13
 * Time: 4:36 PM
 */
public interface TwoPhaseActivityBuilder extends AppUserOwnedObjectBuilder<TwoPhaseActivity> {
    TwoPhaseActivityBuilder withActivityType(final TwoPhaseActivity.Activity activityType);

    TwoPhaseActivityBuilder withExpirationTime(final DateTime expirationTime);
}
