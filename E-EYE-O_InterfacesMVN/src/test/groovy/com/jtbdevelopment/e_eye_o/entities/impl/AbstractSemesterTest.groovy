package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.Semester
import org.joda.time.LocalDate
import org.testng.annotations.Test

/**
 * Date: 11/25/13
 * Time: 9:15 PM
 */
abstract class AbstractSemesterTest extends AbstractAppUserOwnedObjectTest {

    @Test
    void testDescription() {
        testStringFieldExpectingErrorForNullOrBlank("description", "", Semester.SEMESTER_DESCRIPTION_CANNOT_BE_BLANK_OR_NULL_ERROR)
        testStringFieldSize("description", IdObject.MAX_DESCRIPTION_SIZE, Semester.SEMESTER_DESCRIPTION_SIZE_ERROR)
    }

    @Test
    void testStart() {
        testNonStringFieldWithNullValidationError("start", objectUnderTest.start, new LocalDate(), Semester.SEMESTER_START_CANNOT_BE_NULL_ERROR)
    }

    @Test
    void testEnd() {
        testNonStringFieldWithNullValidationError("end", objectUnderTest.end, new LocalDate(), Semester.SEMESTER_END_CANNOT_BE_NULL_ERROR)
    }

    @Test
    void testSummaryDescription() {
        objectUnderTest.description = "TEST"
        assert objectUnderTest.description == objectUnderTest.summaryDescription
    }
}
