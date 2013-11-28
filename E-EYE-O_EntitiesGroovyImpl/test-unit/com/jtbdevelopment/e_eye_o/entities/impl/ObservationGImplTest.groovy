package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.Observation
import com.jtbdevelopment.e_eye_o.entities.ObservationCategory
import com.jtbdevelopment.e_eye_o.entities.Student
import org.joda.time.LocalDateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 11/27/13
 * Time: 6:46 AM
 */
class ObservationGImplTest extends AppUserOwnedObjectGImplTest {
    @BeforeMethod
    def setUp() {
        objectUnderTest = new ObservationGImpl()
    }

    @Test
    public void testObservationSubject() {
        testNonStringFieldWithNullValidationError("observationSubject", null, new StudentGImpl(), Observation.OBSERVATION_OBSERVATION_SUBJECT_CANNOT_BE_NULL_ERROR)
    }

    @Test
    public void testObservationTimestamp() throws Exception {
        LocalDateTime before = LocalDateTime.now();
        objectUnderTest = new ObservationGImpl()
        LocalDateTime after = LocalDateTime.now()

        assert before.compareTo(objectUnderTest.observationTimestamp) <= 0
        assert after.compareTo(objectUnderTest.observationTimestamp) >= 0

        testNonStringFieldWithNullValidationError("observationTimestamp", objectUnderTest.observationTimestamp, new LocalDateTime(), Observation.OBSERVATION_OBSERVATION_TIMESTAMP_CANNOT_BE_NULL_ERROR)
    }

    @Test
    void testSignificant() {
        testBooleanField("significant", true)
    }

    @Test
    void testComments() {
        testStringFieldExpectingErrorForNullOrBlank("comment", "", Observation.OBSERVATION_COMMENT_CANNOT_BE_BLANK_OR_NULL_ERROR)
        testStringFieldSize("comment", Observation.MAX_COMMENT_SIZE, Observation.OBSERVATION_COMMENT_SIZE_ERROR)
    }

    @Test
    void testCategories() {
        testSetField("categories", "Category", new ObservationCategoryGImpl(id: "1"), new ObservationCategoryGImpl(id: "2"), Observation.OBSERVATION_CATEGORIES_CANNOT_CONTAIN_NULL_ERROR)
    }

    @Test
    public void testSummaryDescription() {
        Student student = new StudentGImpl();
        student.setFirstName("First");
        student.setLastName("Last");
        ObservationCategory category1 = new ObservationCategoryGImpl();
        category1.setShortName("ONE ");
        ObservationCategory category2 = new ObservationCategoryGImpl();
        category2.setShortName("TWO ");
        ObservationCategory category3 = new ObservationCategoryGImpl();
        category3.setShortName(null);
        LocalDateTime now = LocalDateTime.now();
        objectUnderTest.setObservationTimestamp(now);
        objectUnderTest.setObservationSubject(student);
        objectUnderTest.addCategory(category1);
        objectUnderTest.addCategory(category2);
        objectUnderTest.addCategory(category3);

        String okBase = student.getSummaryDescription() +
                ' on ' +
                now.toString("YYY-MM-dd") +
                ' for ';

        String ok1 = okBase +
                "ONE, TWO";

        String ok2 = okBase +
                "TWO, ONE";

        assert ok1 == objectUnderTest.summaryDescription || ok2 == objectUnderTest.summaryDescription
    }

    @Test
    public void testSummaryDescriptionNullSubject() {
        ObservationCategory category1 = new ObservationCategoryGImpl();
        category1.setShortName("ONE");
        ObservationCategory category2 = new ObservationCategoryGImpl();
        category2.setShortName("TWO");
        LocalDateTime now = LocalDateTime.now();
        objectUnderTest.setObservationTimestamp(now);
        objectUnderTest.addCategory(category1);
        objectUnderTest.addCategory(category2);

        String okBase = "? on " +
                now.toString("YYY-MM-dd") +
                " for ";
        String ok1 = okBase +
                "ONE, TWO";
        String ok2 = okBase +
                "TWO, ONE";

        assert ok1 == objectUnderTest.summaryDescription || ok2 == objectUnderTest.summaryDescription
    }
}
