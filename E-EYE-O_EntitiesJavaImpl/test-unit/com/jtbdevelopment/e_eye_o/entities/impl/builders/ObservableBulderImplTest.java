package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.Observable;
import com.jtbdevelopment.e_eye_o.entities.Student;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Date: 5/31/13
 * Time: 10:05 PM
 */
public class ObservableBulderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private class ObservableBuilderImplExt extends ObservableBulderImpl {

        private ObservableBuilderImplExt(final Observable entity) {
            super(entity);
        }
    }

    private final Student student = factory.newStudent(null);
    private final ObservableBuilderImplExt builder = new ObservableBuilderImplExt(student);

    @Test
    public void testWithLastObservationTime() {
        assertEquals(Observable.NEVER_OBSERVED, student.getLastObservationTimestamp());
        LocalDateTime ldt = LocalDateTime.now();
        builder.withLastObservationTimestamp(ldt);
        assertEquals(ldt, student.getLastObservationTimestamp());
    }
}
