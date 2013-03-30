package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import org.joda.time.LocalDateTime;

/**
 * Date: 3/29/13
 * Time: 6:49 PM
 */
public abstract class ObservableImpl extends AppUserOwnedObjectImpl implements Observable {

    private LocalDateTime lastObservationTime = Observable.NEVER_OBSERVED;

    protected ObservableImpl(AppUser appUser) {
        super(appUser);
    }

    /**
     * Last Observation Time on Observable
     * <p/>
     * It is anticipated that the dao layer will maintain this automatically
     *
     * @return last observation time
     */
    @Override
    public LocalDateTime getLastObservationTime() {
        return lastObservationTime;
    }

    @Override
    public void setLastObservationTime(final LocalDateTime lastObservationTime) {
        this.lastObservationTime = lastObservationTime;
    }
}
