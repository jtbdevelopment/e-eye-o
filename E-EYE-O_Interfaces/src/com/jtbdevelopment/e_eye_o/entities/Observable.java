package com.jtbdevelopment.e_eye_o.entities;

import org.joda.time.LocalDateTime;

import javax.validation.constraints.NotNull;

/**
 * Date: 3/29/13
 * Time: 6:39 PM
 */
//  TODO - make lastObTimestamp not modifiable except via controlled routes
public interface Observable extends AppUserOwnedObject {
    public final static LocalDateTime NEVER_OBSERVED = UNINITIALISED_LOCAL_DATE_TIME;

    public final static String LAST_OBSERVATION_TIME_CANNOT_BE_NULL = "Observable.lastObservationTimestamp" + CANNOT_BE_NULL_ERROR;

    @NotNull(message = Observable.LAST_OBSERVATION_TIME_CANNOT_BE_NULL)
    LocalDateTime getLastObservationTimestamp();

    void setLastObservationTimestamp(final LocalDateTime lastObservationTimestamp);
}
