package com.jtbdevelopment.e_eye_o.entities.builders

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import org.joda.time.DateTime

/**
 * Date: 12/1/13
 * Time: 3:36 PM
 */
class TwoPhaseActivityBuilderGImpl extends AppUserOwnedObjectBuilderGImpl<TwoPhaseActivity> implements TwoPhaseActivityBuilder {
    @Override
    TwoPhaseActivityBuilder withActivityType(final TwoPhaseActivity.Activity activityType) {
        entity.activityType = activityType
        return this
    }

    @Override
    TwoPhaseActivityBuilder withExpirationTime(final DateTime expirationTime) {
        entity.expirationTime = expirationTime
        return this
    }
}
