package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 3/9/13
 * Time: 12:24 PM
 */
public class ObservationCategoryBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final ObservationCategory impl = factory.newObservationCategory(null);
    private final ObservationCategoryBuilderImpl builder = new ObservationCategoryBuilderImpl(impl);

    @Test
    public void testWithShortName() throws Exception {
        assertEquals("", impl.getShortName());
        final String value = "s";
        assertSame(builder, builder.withShortName(value));
        assertEquals(value, impl.getShortName());
    }

    @Test
    public void testWithDescription() throws Exception {
        assertEquals("", impl.getDescription());
        final String value = "desc";
        assertSame(builder, builder.withDescription(value));
        assertEquals(value, impl.getDescription());
    }
}
