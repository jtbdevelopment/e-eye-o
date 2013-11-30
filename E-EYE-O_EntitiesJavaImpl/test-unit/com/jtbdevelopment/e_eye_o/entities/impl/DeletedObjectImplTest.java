package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.DeletedObject;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Date: 2/16/13
 * Time: 7:58 PM
 */
public class DeletedObjectImplTest extends AbstractAppUserOwnedObjectTest<DeletedObjectImpl> {
    public DeletedObjectImplTest() {
        super(DeletedObjectImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testSetGetDeletedId() {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("deletedId", DeletedObject.DELETED_OBJECT_DELETED_ID_CANNOT_BE_BLANK_OR_NULL_ERROR);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testCannotSetNewDeletedId() {
        DeletedObjectImpl impl = new DeletedObjectImpl(null);
        final String x = "X";
        impl.setDeletedId(x);
        assertEquals(x, impl.getDeletedId());
        impl.setDeletedId("Y");
    }

    @Test
    public void testSummaryDescription() {
        DeletedObjectImpl impl = new DeletedObjectImpl(null);
        final String x = "X";
        impl.setDeletedId(x);
        assertEquals(x, impl.getSummaryDescription());
    }
}
