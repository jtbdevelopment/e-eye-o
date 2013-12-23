package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 11/18/13
 * Time: 9:06 PM
 */
abstract class AbstractIdObjectTest extends AbstactIdObjectBaseTest {
    abstract <T extends IdObject> T createObjectUnderTest()

    @BeforeMethod
    def setup() {
        objectUnderTest = createObjectUnderTest()
    }

    protected testStringFieldSize(def String field, def int maxSize, def String maxSizeError) {
        def String shortString = "S";
        objectUnderTest."$field" = shortString;
        testValidation([], [maxSizeError])
        objectUnderTest."$field" = shortString.padRight(maxSize + 1, "+");
        testValidation([maxSizeError], [])
    }

    @Test(expectedExceptions = ReadOnlyPropertyException.class)
    void testSummaryDescriptionIsReadOnly() {
        objectUnderTest.summaryDescription = "TEST"
    }

    @Test
    void testSummaryDescription() {
        objectUnderTest.id = GENERAL_STRING;
        assert GENERAL_STRING == objectUnderTest.summaryDescription
    }

    @Test
    void testId() {
        testStringFieldExpectingErrorForNullOrBlank("id", null, IdObject.ID_OBJECT_ID_MAY_NOT_BE_EMPTY_ERROR);
    }

    @Test
    void testHashCode() {
        objectUnderTest.id = GENERAL_STRING;
        assert GENERAL_STRING.hashCode() == objectUnderTest.hashCode()
    }


    @Test
    public void testImplEqualsItself() {
        assert objectUnderTest == objectUnderTest
    }

    @Test
    public void testEqualsNull() {
        assert objectUnderTest != null;
    }

    @Test
    public void testEqualsNonObjectIdType() {
        def s = "aString"
        assert objectUnderTest != s;
    }

    @Test
    public void testObjectsEquals() {
        IdObject impl1 = createObjectUnderTest()
        IdObject impl2 = createObjectUnderTest()
        IdObject impl3 = createObjectUnderTest()

        assert impl1 != impl2;
        assert impl2 != impl1;

        impl1.id = GENERAL_STRING.toUpperCase();
        assert impl1 != impl2;
        assert impl2 != impl1;

        impl2.id = GENERAL_STRING.toUpperCase();
        assert impl1 == impl2;
        assert impl2 == impl1;


        impl3.id = GENERAL_STRING.toLowerCase()
        assert impl1 != impl3
        assert impl3 != impl1
    }

    @Test
    void testModificationTimestamp() {
        DateTime before = DateTime.now();
        IdObject test = createObjectUnderTest()
        DateTime after = DateTime.now();

        assert test.modificationTimestamp != null
        assert before.compareTo(test.modificationTimestamp) <= 0
        assert after.compareTo(test.modificationTimestamp) >= 0

        DateTime set = before.minusDays(1)
        test.modificationTimestamp = set;
        assert set == test.modificationTimestamp
    }

    @Test(expectedExceptions = IllegalStateException.class)
    void testCannotSetNonNullId() {
        IdObject test = createObjectUnderTest()
        test.id = GENERAL_STRING
        test.id = "NEW ID"
    }

    @Test
    void testResettingIdToSameValueWorks() {
        IdObject test = createObjectUnderTest()
        test.id = GENERAL_STRING
        test.id = (GENERAL_STRING + "")
    }
}
