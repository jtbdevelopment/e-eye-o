package com.jtbdevelopment.e_eye_o.entities.impl.builders

import com.jtbdevelopment.e_eye_o.entities.impl.DeletedObjectGImpl
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 12/1/13
 * Time: 8:27 PM
 */
class DeletedObjectBuilderGImplTest extends AppUserOwnedObjectBuilderGImplTest {

    @BeforeMethod
    def setUp() {
        entity = new DeletedObjectGImpl()
        builder = new DeletedObjectBuilderGImpl(entity: entity)
    }

    @Test
    void testWithDeletedId() {
        testStringField("deletedId")
    }
}
