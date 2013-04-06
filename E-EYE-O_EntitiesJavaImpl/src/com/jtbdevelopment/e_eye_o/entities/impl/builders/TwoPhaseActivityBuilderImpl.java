package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import com.jtbdevelopment.e_eye_o.entities.builders.TwoPhaseActivityBuilder;
import org.joda.time.DateTime;

/**
 * Date: 4/6/13
 * Time: 4:35 PM
 */
public class TwoPhaseActivityBuilderImpl extends AppUserOwnedObjectBuilderImpl<TwoPhaseActivity> implements TwoPhaseActivityBuilder {
    public TwoPhaseActivityBuilderImpl(final TwoPhaseActivity entity) {
        super(entity);
    }

    @Override
    public TwoPhaseActivityBuilder withActivityType(final TwoPhaseActivity.Activity activityType) {
        entity.setActivityType(activityType);
        return this;
    }

    @Override
    public TwoPhaseActivityBuilder withExpirationTime(final DateTime expirationTime) {
        entity.setExpirationTime(expirationTime);
        return this;
    }
}
