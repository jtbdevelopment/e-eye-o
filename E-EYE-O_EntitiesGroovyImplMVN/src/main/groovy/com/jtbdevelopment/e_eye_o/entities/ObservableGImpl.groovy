package com.jtbdevelopment.e_eye_o.entities

import groovy.transform.CompileStatic
import org.joda.time.LocalDateTime

/**
 * Date: 11/26/13
 * Time: 6:44 AM
 */
@CompileStatic
abstract class ObservableGImpl extends AppUserOwnedObjectGImpl implements Observable {
    LocalDateTime lastObservationTimestamp = Observable.NEVER_OBSERVED
}
