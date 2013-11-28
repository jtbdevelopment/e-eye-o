package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.Observable
import org.joda.time.LocalDateTime

/**
 * Date: 11/26/13
 * Time: 6:44 AM
 */
abstract class ObservableGImpl extends AppUserOwnedObjectGImpl implements Observable {
    LocalDateTime lastObservationTimestamp = Observable.NEVER_OBSERVED
}
