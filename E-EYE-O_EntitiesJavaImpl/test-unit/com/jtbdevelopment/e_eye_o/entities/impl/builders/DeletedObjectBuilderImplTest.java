package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 3/9/13
 * Time: 12:03 PM
 */
public class DeletedObjectBuilderImplTest extends AppUserOwnedObjectBuilderImplTest {
    private final DeletedObject impl = factory.newDeletedObject(null);
    private final DeletedObjectBuilderImpl builder = new DeletedObjectBuilderImpl(impl);

    @Test
    public void testWithDeletedId() throws Exception {
        assertEquals("", impl.getDeletedId());
        final String value = "deleted";
        assertSame(builder, builder.withDeletedId(value));
        assertEquals(value, impl.getDeletedId());
    }
}
