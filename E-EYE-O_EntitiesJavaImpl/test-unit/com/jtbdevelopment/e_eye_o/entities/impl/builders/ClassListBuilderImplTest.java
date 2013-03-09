package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.ClassList;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 3/9/13
 * Time: 12:08 PM
 */
public class ClassListBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final ClassList impl = factory.newClassList(null);
    private final ClassListBuilderImpl builder = new ClassListBuilderImpl(impl);

    @Test
    public void testWithDescription() throws Exception {
        final String value = "desc";
        assertEquals("", impl.getDescription());
        assertSame(builder, builder.withDescription(value));
        assertEquals(value, impl.getDescription());
    }
}
