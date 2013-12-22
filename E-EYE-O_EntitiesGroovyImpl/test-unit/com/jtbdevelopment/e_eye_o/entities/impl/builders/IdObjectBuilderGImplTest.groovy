package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectGImpl
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 3:57 PM
 */
class IdObjectBuilderGImplTest extends AbstractBuilderGImplTest {

    static class LocalEntityImpl extends IdObjectGImpl {
    }

    @BeforeMethod
    def setUp() {
        entity = new LocalEntityImpl()
        builder = new IdObjectBuilderGImpl(entity: entity)
    }

    @Test
    public void testWithId() throws Exception {
        testStringField("id")
    }

    @Test
    public void testWithModificationTimestamp() throws Exception {
        testField("modificationTimestamp", DateTime.now())
    }
}
