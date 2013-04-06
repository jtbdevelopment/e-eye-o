package com.jtbdevelopment.e_eye_o.hibernate.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Date: 4/6/13
 * Time: 2:49 PM
 */
@Entity(name = "TwoPhaseActivity")
public class HibernateTwoPhaseActivity extends HibernateAppUserOwnedObject<TwoPhaseActivity> implements TwoPhaseActivity {

    @Override
    @Column(nullable = false)
    public Activity getActivityType() {
        return wrapped.getActivityType();
    }

    @Override
    public void setActivityType(final Activity activityType) {
        wrapped.setActivityType(activityType);
    }

    @Override
    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getExpirationTime() {
        return wrapped.getExpirationTime();
    }

    @Override
    public void setExpirationTime(final DateTime expirationTime) {
        wrapped.setExpirationTime(expirationTime);
    }
}
