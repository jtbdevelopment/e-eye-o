package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.DateTime;

/**
 * Date: 4/6/13
 * Time: 2:46 PM
 */
public class TwoPhaseActivityImpl extends AppUserOwnedObjectImpl implements TwoPhaseActivity {
    private Activity activityType;
    private DateTime expirationTime = new DateTime();

    public TwoPhaseActivityImpl(final AppUser appUser) {
        super(appUser);
    }

    @Override
    public Activity getActivityType() {
        return activityType;
    }

    @Override
    public void setActivityType(final Activity activityType) {
        this.activityType = activityType;
    }

    @Override
    public DateTime getExpirationTime() {
        return expirationTime;
    }

    @Override
    public void setExpirationTime(final DateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Override
    public String getSummaryDescription() {
        return "Id="
                + getId()
                + " For="
                + getAppUser().getSummaryDescription()
                + " Type="
                + getActivityType().toString()
                + " Expiration="
                + getExpirationTime().toString("YYYY-MM-dd HH:mm:ss")
                + " Archived="
                + isArchived();
    }
}
