package com.jtbdevelopment.e_eye_o.entities;

import org.testng.annotations.Test;

/**
 * Date: 12/8/12
 * Time: 2:53 PM
 */
public class ObservationCategoryImplTest extends AbstractAppUserOwnedObjectTest {
    @Test
    public void testConstructors() {
        checkDefaultAndAppUserConstructorTests(ObservationCategoryImpl.class);
    }

    @Test
    public void testSetGetShortName() throws Exception {
        checkStringSetGetsAndValidateNullsAsError(ObservationCategoryImpl.class, "shortName", ObservationCategory.OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testShortNameSize() throws Exception {
        checkStringSizeValidation(ObservationCategoryImpl.class, "shortName", TOO_LONG_FOR_SHORT_NAME,ObservationCategory.OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR);
    }

    @Test
    public void testSetGetDescription() throws Exception {
//        checkStringSetGetsWithNullsSavedAsBlanks(ObservationCategoryImpl.class, "description");
    }
}
