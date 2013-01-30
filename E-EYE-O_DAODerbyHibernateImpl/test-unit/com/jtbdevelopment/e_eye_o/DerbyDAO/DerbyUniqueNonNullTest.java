package com.jtbdevelopment.e_eye_o.DerbyDAO;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 1/6/13
 * Time: 9:49 PM
 */
public class DerbyUniqueNonNullTest {
    @Test
    public void testSupportsNotNullUnique() throws Exception {
        assertTrue(new DerbyUniqueNonNull().supportsNotNullUnique());
    }
}
