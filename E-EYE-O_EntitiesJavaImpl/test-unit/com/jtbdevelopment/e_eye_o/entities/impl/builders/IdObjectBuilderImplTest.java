package com.jtbdevelopment.e_eye_o.entities.impl.builders;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectFactoryImpl;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImpl;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 3/9/13
 * Time: 11:39 AM
 */
public class IdObjectBuilderImplTest {
    public interface LocalEntity extends IdObject {
    }

    public class LocalEntityImpl extends IdObjectImpl implements LocalEntity {
    }

    protected IdObjectFactory factory = new IdObjectFactoryImpl();
    private final LocalEntityImpl impl = new LocalEntityImpl();
    private final IdObjectBuilderImpl<LocalEntity> builder = new IdObjectBuilderImpl<LocalEntity>(impl);

    @Test
    public void testBuild() throws Exception {
        assertSame(impl, builder.build());
    }

    @Test
    public void testWithId() throws Exception {
        final String xyz = "XYZ";
        assertSame(builder, builder.withId(xyz));
        assertEquals(xyz, impl.getId());
    }

    @Test
    public void testWithModificationTimestamp() throws Exception {
        final DateTime modTime = new DateTime();
        assertSame(builder, builder.withModificationTimestamp(modTime));
        assertEquals(modTime, impl.getModificationTimestamp());
    }
}
