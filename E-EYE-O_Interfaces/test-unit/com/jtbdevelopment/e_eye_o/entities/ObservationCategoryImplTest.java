package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/8/12
 * Time: 2:53 PM
 */
public class ObservationCategoryImplTest extends AbstractIdObjectTest {
    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(ClassListImpl.class);
    }

    @Test
    public void testSetGetShortName() throws Exception {
        checkStringSetGetsWithNullsSavedAsBlanks(ObservationCategoryImpl.class, "shortName");
    }

    @Test
    public void testSetGetDescription() throws Exception {
        checkStringSetGetsWithNullsSavedAsBlanks(ObservationCategoryImpl.class, "description");
    }
}
