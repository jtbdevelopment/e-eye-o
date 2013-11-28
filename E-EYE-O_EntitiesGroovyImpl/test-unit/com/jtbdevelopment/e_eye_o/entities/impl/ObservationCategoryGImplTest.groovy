package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 11/26/13
 * Time: 6:58 AM
 */
class ObservationCategoryGImplTest extends AppUserOwnedObjectGImplTest {
    @BeforeMethod
    def setUp() {
        objectUnderTest = new ObservationCategoryGImpl()
    }

    @Test
    public void testShortName() throws Exception {
        testStringFieldExpectingErrorForNullOrBlank("shortName", "", ObservationCategory.OBSERVATION_CATEGORY_SHORT_NAME_CANNOT_BE_BLANK_OR_NULL)
        testStringFieldSize("shortName", IdObject.MAX_SHORT_NAME_SIZE, ObservationCategory.OBSERVATION_CATEGORY_SHORT_NAME_SIZE_ERROR)
    }

    @Test
    public void testDescription() throws Exception {
        testStringFieldExpectingErrorForNullOrBlank("description", "", ObservationCategory.OBSERVATION_CATEGORY_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL)
        testStringFieldSize("description", IdObject.MAX_DESCRIPTION_SIZE, ObservationCategory.OBSERVATION_CATEGORY_DESCRIPTION_SIZE_ERROR)
    }

    @Test
    public void testSummaryDescription() {
        objectUnderTest.description = " A Desc "
        assert "A Desc" == objectUnderTest.summaryDescription
    }
}
