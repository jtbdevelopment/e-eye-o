package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.DeletedObject
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

/**
 * Date: 11/30/13
 * Time: 4:16 PM
 */
abstract class AbstractDeletedObjectTest extends AbstractAppUserOwnedObjectTest {

    @Test
    public void testDeletedId() {
        testStringFieldExpectingErrorForNullOrBlank("deletedId", "", DeletedObject.DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR)
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testCannotSetNewDeletedId() {
        final String x = "X";
        objectUnderTest.setDeletedId(x);
        assert x == objectUnderTest.deletedId;
        objectUnderTest.setDeletedId("Y");
    }

    @Test
    public void testSummaryDescription() {
        final String x = "X";
        objectUnderTest.setDeletedId(x);
        assertEquals(x, objectUnderTest.getSummaryDescription());
    }
}
