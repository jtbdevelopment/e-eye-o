package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.TwoPhaseActivity
import groovy.transform.CompileStatic
import org.joda.time.DateTime

/**
 * Date: 11/30/13
 * Time: 12:25 PM
 */
@CompileStatic
class TwoPhaseActivityGImpl extends AppUserOwnedObjectGImpl implements TwoPhaseActivity {
    TwoPhaseActivity.Activity activityType
    DateTime expirationTime = DateTime.now()

    @Override
    String getSummaryDescription() {
        return "Id=" +
                getId() +
                " For=" +
                getAppUser().getSummaryDescription() +
                " Type=" +
                getActivityType().toString() +
                " Expiration=" +
                getExpirationTime().toString("YYYY-MM-dd HH:mm:ss") +
                " Archived=" +
                isArchived();
    }
}
