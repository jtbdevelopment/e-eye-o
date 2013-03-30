package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;

/**
 * Date: 3/29/13
 * Time: 6:39 PM
 */
public interface Observable extends AppUserOwnedObject {
    public final static LocalDateTime NEVER_OBSERVED = UNINITIALISED_LOCAL_DATE_TIME;

    public final static String LAST_OBSERVED_TIME = "Observable.lastObservationTime" + CANNOT_BE_NULL_ERROR;

    @NotNull(message = Observable.LAST_OBSERVED_TIME)
    LocalDateTime getLastObservationTime();

    void setLastObservationTime(final LocalDateTime lastObservationTime);
}
