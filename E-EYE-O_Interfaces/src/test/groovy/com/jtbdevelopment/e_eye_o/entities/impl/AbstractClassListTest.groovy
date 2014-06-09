package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.ClassList
import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.testng.annotations.Test

/**
 * Date: 11/26/13
 * Time: 6:52 AM
 */
abstract class AbstractClassListTest extends AbstractObservableTest {

    @Test
    public void testDescription() {
        testStringFieldExpectingErrorForNullOrBlank("description", "", ClassList.CLASS_LIST_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
        testStringFieldSize("description", IdObject.MAX_DESCRIPTION_SIZE, ClassList.CLASS_LIST_DESCRIPTION_SIZE_ERROR)
    }

    @Test
    public void testSummaryDescription() {
        objectUnderTest.description = "DESCRipTIOn"
        assert objectUnderTest.description == objectUnderTest.summaryDescription
    }
}
