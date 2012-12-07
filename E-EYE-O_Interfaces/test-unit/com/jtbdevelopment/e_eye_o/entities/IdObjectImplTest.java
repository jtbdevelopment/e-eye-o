package com.jtbdevelopment.e_eye_o.entities;

import static junit.framework.Assert.*;
import org.junit.Test;
import sun.plugin.dom.exception.InvalidStateException;

import java.security.InvalidParameterException;

/**
 * Date: 12/2/12
 * Time: 1:12 AM
 */
public class IdObjectImplTest {
    private class IdObjectExtends extends IdObjectImpl {

    }
    private static final String ID = "SOMETHING";

    @Test
    public void testEquals() throws Exception {

    }

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

    @Test(expected = InvalidParameterException.class)
    public void testSetIdNullExceptions() throws Exception {
        IdObjectExtends local = new IdObjectExtends();
        local.setId(null);
    }

    @Test
    public void testResettingIdWorks() throws Exception {
        IdObjectExtends local = new IdObjectExtends();
        local.setId(ID);
        local.setId(ID + "");
    }

    @Test(expected = InvalidStateException.class)
    public void testAssigningNewIdFails() {
        IdObjectExtends local = new IdObjectExtends();
        local.setId(ID);
        local.setId(ID + ID);
        fail("Should have exception");
    }
}
