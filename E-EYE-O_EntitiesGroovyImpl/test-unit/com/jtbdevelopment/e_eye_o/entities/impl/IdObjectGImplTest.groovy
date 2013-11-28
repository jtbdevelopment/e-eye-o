package com.jtbdevelopment.e_eye_o.entities.impl

import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.joda.time.DateTime
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * Date: 11/18/13
 * Time: 9:06 PM
 */
class IdObjectGImplTest extends AbstactIdObjectGImplTest {
    public static class IdObjectGTest extends IdObjectGImpl {

    }

    @BeforeMethod
    def setup() {
        objectUnderTest = new IdObjectGTest()
    }

    protected testStringFieldSize(def String field, def int maxSize, def String maxSizeError) {
        def String shortString = "S";
        objectUnderTest."$field" = shortString;
        testValidation([], [maxSizeError])
        objectUnderTest."$field" = shortString.padRight(maxSize + 1, "+");
        testValidation([maxSizeError], [])
    }

    @Test(expectedExceptions = ReadOnlyPropertyException.class)
    void testSummaryDescription() {
        objectUnderTest.id = GENERAL_STRING
        testField "summaryDescription", GENERAL_STRING, GENERAL_STRING
    }

    @Test
    void testSummaryDescriptionIsId() {
        //  Under groovy, it appears all tests are rerun for derived classes
        if (objectUnderTest instanceof IdObjectGTest) {
            objectUnderTest.id = GENERAL_STRING;
            assert GENERAL_STRING == objectUnderTest.summaryDescription
        }
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
        IdObjectGTest impl1 = new IdObjectGTest();
        IdObjectGTest impl2 = new IdObjectGTest();
        IdObjectGTest impl3 = new IdObjectGTest();

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
        IdObjectGTest test = new IdObjectGTest()
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
        IdObjectGTest test = new IdObjectGTest(id: GENERAL_STRING);
        test.id = "NEW ID"
    }

    @Test
    void testResettingIdToSameValueWorks() {
        IdObjectGTest test = new IdObjectGTest(id: GENERAL_STRING)
        test.id = (GENERAL_STRING + "")
    }
}
