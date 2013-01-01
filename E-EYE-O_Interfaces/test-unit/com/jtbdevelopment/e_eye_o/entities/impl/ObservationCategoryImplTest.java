package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.ObservationCategory;
import org.testng.annotations.Test;

/**
 * Date: 12/8/12
 * Time: 2:53 PM
 */
public class ObservationCategoryImplTest extends AbstractAppUserOwnedObjectTest<ObservationCategoryImpl> {
    public ObservationCategoryImplTest() {
        super(ObservationCategoryImpl.class);
    }

    @Test
    public void testConstructorsForNewObjects() {
        checkDefaultAndAppUserConstructorTests();
    }

    @Test
    public void testSetGetShortName() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("shortName", ObservationCategory.OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testShortNameSize() throws Exception {
        checkStringSizeValidation("shortName", TOO_LONG_FOR_SHORT_NAME, ObservationCategory.OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR);
    }

    @Test
    public void testSetGetDescription() throws Exception {
        checkStringSetGetsAndValidateNullsAndBlanksAsError("description", ObservationCategory.OBSERVATION_CATEGORY_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL);
    }

    @Test
    public void testDescriptionSize() throws Exception {
        checkStringSizeValidation("description", TOO_LONG_FOR_DESCRIPTION, ObservationCategory.OBSERVATION_CATEGORY_DESCRIPTION_SIZE_ERROR);
    }
}
