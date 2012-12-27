package com.jtbdevelopment.e_eye_o.entities;


import org.testng.annotations.Test;
import sun.plugin.dom.exception.InvalidStateException;

import static org.testng.AssertJUnit.*;


/**
 * Date: 12/2/12
 * Time: 1:12 AM
 */
public class IdObjectImplTest extends AbstractAppUserOwnedObjectTest {
    private class IdObjectExtends extends IdObjectImpl {

    }

    private static final String ID = "SOMETHING";

    @Test
    public void testHashCode() throws Exception {
        IdObjectExtends local = new IdObjectExtends();
        assertEquals(0, local.hashCode());
        local.setId(ID);
        assertEquals(ID.hashCode(), local.hashCode());
    }

    @Test
    public void testImplEqualsItself() {
        IdObjectExtends local = new IdObjectExtends();
        assertTrue(local.equals(local));
    }

    @Test
    public void testEqualsNull() {
        IdObjectExtends local = new IdObjectExtends();
        assertFalse(local.equals(null));
    }

    @Test
    public void testEqualsNonObjectIdType() {
        IdObjectExtends local = new IdObjectExtends();
        String s = "";
        assertFalse(local.equals(s));
    }

    @Test
    public void testObjectsEquals() {
        IdObjectExtends impl1 = new IdObjectExtends();
        IdObjectExtends impl2 = new IdObjectExtends();
        IdObjectExtends impl3 = new IdObjectExtends();

        assertFalse(impl1.equals(impl2));
        assertFalse(impl2.equals(impl1));

        impl1.setId(ID);
        assertFalse(impl1.equals(impl2));
        assertFalse(impl2.equals(impl1));

        impl2.setId(ID);
        assertTrue(impl1.equals(impl2));
        assertTrue(impl2.equals(impl1));

        impl3.setId(ID.toLowerCase());
        assertFalse(impl1.equals(impl3));
        assertFalse(impl3.equals(impl1));
    }

    @Test
    public void testSetIdNullExceptions() throws Exception {
        IdObjectExtends local = new IdObjectExtends();
        local.setId(null);
        validateExpectingError(local, IdObject.ID_OBJECT_ID_MAY_NOT_BE_EMPTY_ERROR);
    }

    @Test
    public void testResettingIdWorks() throws Exception {
        IdObjectExtends local = new IdObjectExtends();
        local.setId(ID);
        local.setId(ID + "");
    }

    @Test(expectedExceptions = {InvalidStateException.class})
    public void testAssigningNewIdFails() {
        IdObjectExtends local = new IdObjectExtends();
        local.setId(ID);
        local.setId(ID + ID);
        fail("Should have exception");
    }
}
