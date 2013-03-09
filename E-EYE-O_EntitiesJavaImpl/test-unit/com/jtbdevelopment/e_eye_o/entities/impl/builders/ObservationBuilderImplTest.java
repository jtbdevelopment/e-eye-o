package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import com.jtbdevelopment.e_eye_o.entities.Observation;
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * Date: 3/9/13
 * Time: 12:29 PM
 */
public class ObservationBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final Observation impl = factory.newObservation(null);
    private final Observation fu = factory.newObservation(null);
    private final ObservationCategory c1 =factory.newObservationCategory(null);
    private final ObservationCategory c2 = factory.newObservationCategory(null);
    private final ClassList cl = factory.newClassList(null);
    private final ObservationBuilderImpl builder = new ObservationBuilderImpl(impl);

    @Test
    public void testWithObservationSubject() throws Exception {
        assertNull(impl.getObservationSubject());
        assertSame(builder, builder.withObservationSubject(cl));
        assertSame(cl, impl.getObservationSubject());
    }

    @Test
    public void testWithObservationTimestamp() throws Exception {
        final LocalDateTime time = new LocalDateTime();
        impl.setObservationTimestamp(time.minusDays(200));
        assertSame(builder, builder.withObservationTimestamp(time));
        assertEquals(time, impl.getObservationTimestamp());
    }

    @Test
    public void testWithSignificant() throws Exception {
        impl.setSignificant(false);
        assertSame(builder, builder.withSignificant(true));
        assertTrue(impl.isSignificant());
    }

    @Test
    public void testWithFollowUpNeeded() throws Exception {
        impl.setFollowUpNeeded(false);
        assertSame(builder, builder.withFollowUpNeeded(true));
        assertTrue(impl.isFollowUpNeeded());
    }

    @Test
    public void testWithFollowUpReminder() throws Exception {
        LocalDate date = new LocalDate();
        assertNull(impl.getFollowUpReminder());
        assertSame(builder, builder.withFollowUpReminder(date));
        assertEquals(date, impl.getFollowUpReminder());
    }

    @Test
    public void testWithFollowUpObservation() throws Exception {
        assertNull(impl.getFollowUpObservation());
        assertSame(builder, builder.withFollowUpObservation(fu));
        assertSame(fu, impl.getFollowUpObservation());
    }

    @Test
    public void testAddCategory() throws Exception {
        synchronized (impl) {
            impl.setCategories(Collections.EMPTY_SET);
            assertSame(builder, builder.addCategory(c1  ));
            assertEquals(1, impl.getCategories().size());
            assertTrue(impl.getCategories().contains(c1));
        }
    }

    @Test
    public void testWithCategories() throws Exception {
        synchronized (impl) {
            impl.setCategories(Collections.EMPTY_SET);
            Set<ObservationCategory> set = new HashSet<>();
            set.add(c1);
            set.add(c2);
            assertSame(builder, builder.withCategories(set));
            assertEquals(set.size(), impl.getCategories().size());
            assertTrue(impl.getCategories().containsAll(set));
        }
    }

    @Test
    public void testWithComment() throws Exception {
        final String c = "c";
        assertEquals("", impl.getComment());
        assertSame(builder, builder.withComment(c));
        assertEquals(c, impl.getComment());
    }
}
