package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.Observable;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Date: 6/1/13
 * Time: 11:43 AM
 */
public class ObservableImplTest extends AbstractAppUserOwnedObjectTest<ObservableImpl> {
    private class ObservableImplExtend extends ObservableImpl {
        private ObservableImplExtend(final AppUser appUser) {
            super(appUser);
        }
    }

    public ObservableImplTest() {
        super(ObservableImpl.class);
    }

    @Test
    public void testDefaultLastObservationTimestamp() {
        assertEquals(Observable.NEVER_OBSERVED, new ObservableImplExtend(USER1).getLastObservationTimestamp());
    }

    @Test
    public void testSetGetLastObservationTimestamp() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ObservableImplExtend local = new ObservableImplExtend(USER1);
        local.setLastObservationTimestamp(now);
        assertEquals(now, local.getLastObservationTimestamp());
    }

    @Test
    public void testSetLastObservationTimestampNullExceptions() throws Exception {
        ObservableImplExtend local = new ObservableImplExtend(USER1);
        local.setLastObservationTimestamp(null);
        validateExpectingError(local, Observable.LAST_OBSERVATION_TIME_CANNOT_BE_NULL);
    }
}
