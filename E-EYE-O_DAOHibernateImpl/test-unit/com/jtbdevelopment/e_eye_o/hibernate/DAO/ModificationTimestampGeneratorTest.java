package com.jtbdevelopment.e_eye_o.hibernate.DAO;

import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.jmock.Mockery;
import org.joda.time.DateTime;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.*;


/**
 * Date: 2/13/13
 * Time: 12:43 AM
 */
public class ModificationTimestampGeneratorTest {
    private IdObject idObject;
    private final ModificationTimestampGenerator interceptor = new ModificationTimestampGenerator();

    @BeforeMethod
    public void setup() {
        Mockery context = new Mockery();
        idObject = context.mock(IdObject.class);
    }

    @Test
    public void testOnFlushDirty() throws Exception {
        final String[] properties = new String[5];
        final Object[] states = new Object[5];
        properties[2] = "modificationTimestamp";
        assertTrue(interceptor.onFlushDirty(idObject, null, states, null, properties, null));
        assertNotNull(states[2]);
        assertTrue(states[2] instanceof DateTime);
    }

    @Test
    public void testOnFlushDirtyNonIdObject() throws Exception {
        final String[] properties = new String[5];
        final Object[] states = new Object[5];
        properties[2] = "modificationTimestamp";
        assertFalse(interceptor.onFlushDirty(new Object(), null, states, null, properties, null));
        assertNull(states[2]);
    }

    @Test
    public void testOnFlushDirtyInstant() throws Exception {
        final String[] properties = new String[5];
        final Object[] states = new Object[5];
        properties[2] = "modificationTimestampInstant";
        assertTrue(interceptor.onFlushDirty(idObject, null, states, null, properties, null));
        assertNotNull(states[2]);
        assertTrue(states[2] instanceof Long);
    }

    @Test
    public void testOnFlushDirtyNonIdObjectInstant() throws Exception {
        final String[] properties = new String[5];
        final Object[] states = new Object[5];
        properties[2] = "modificationTimestampInstant";
        assertFalse(interceptor.onFlushDirty(new Object(), null, states, null, properties, null));
        assertNull(states[2]);
    }

    @Test
    public void testOnSave() throws Exception {
        final String[] properties = new String[5];
        final Object[] states = new Object[5];
        properties[4] = "modificationTimestamp";

        assertTrue(interceptor.onSave(idObject, null, states, properties, null));
        assertNotNull(states[4]);
        assertTrue(states[4] instanceof DateTime);
    }

    @Test
    public void testOnSaveNonIdObject() throws Exception {
        final String[] properties = new String[5];
        final Object[] states = new Object[5];
        properties[1] = "modificationTimestamp";
        assertFalse(interceptor.onSave(new Object(), null, states, properties, null));
        assertNull(states[1]);
    }
}
