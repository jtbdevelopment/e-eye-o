package com.jtbdevelopment.e_eye_o.entities.impl

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

/**
 * Date: 11/25/13
 * Time: 8:45 PM
 *
 * TODO - some version of these tests should become baseline acceptance tests for all implementations
 * instead of implementing per implementation versions
 */
abstract class AbstactIdObjectGImplTest {
    protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    protected static final Validator validator = validatorFactory.getValidator();

    protected objectUnderTest

    def static final String GENERAL_STRING = "A Value"
    def static final String BLANK_STRING = ""
    def static final NULL = null;

    protected testStringFieldExpectingErrorForNullOnly(def String field, def String defaultValue, def String expectedError) {
        testStringFieldWithValidationError(field, defaultValue, expectedError, true)
    }

    protected testStringFieldExpectingErrorForNullOrBlank(def String field, def String defaultValue, def String expectedError) {
        testStringFieldWithValidationError(field, defaultValue, expectedError, false)
    }

    private testStringFieldWithValidationError(def String field, def String defaultValue, def String expectedError, def Boolean blankOK) {
        assert objectUnderTest."$field" == defaultValue;
        objectUnderTest."$field" = NULL;
        testValidation([expectedError], [])
        objectUnderTest."$field" = BLANK_STRING;
        assert BLANK_STRING == objectUnderTest."$field"
        blankOK ? testValidation([], [expectedError]) : testValidation([expectedError], [])
        objectUnderTest."$field" = GENERAL_STRING;
        assert GENERAL_STRING == objectUnderTest."$field"
        testValidation([], [expectedError])
    }

    protected testNonStringFieldWithNullValidationError(def String field, def defaultValue, def testValue, def String expectedError) {
        assert objectUnderTest."$field" == defaultValue;
        objectUnderTest."$field" = NULL;
        testValidation([expectedError], [])
        objectUnderTest."$field" = testValue
        testValidation([], [expectedError])
    }

    protected testBooleanField(def String field, def boolean defaultValue) {
        assert objectUnderTest."$field" == defaultValue;
        objectUnderTest."$field" = !defaultValue
        assert objectUnderTest."$field" == !defaultValue
    }

    protected testValidation(def List<String> expected, def List<String> prohibited) {
        Set<String> violations = validator.validate(objectUnderTest).collect() { it.message }
        def violationsExpected = (expected as Set).intersect(violations);
        def violationsProhibited = (prohibited as Set).intersect(violations);
        assert violationsProhibited.size() == 0: "Prohibited violations were found " + violationsProhibited
        assert violationsExpected.asList() == expected;
    }

    protected testField(def field, def defaultValue, def override) {
        assert objectUnderTest."$field" == defaultValue;
        objectUnderTest."$field" = override;
        assert override == objectUnderTest."$field"
    }

    protected testSetField(def String field, def String upperSingle, def value1, def value2, def String containsNullError) {
        assert objectUnderTest."$field" != null
        assert objectUnderTest."$field".size() == 0
        testValidation([], [containsNullError])

        Set set = [value1, value2] as Set
        objectUnderTest."$field" = set
        assert set == objectUnderTest."$field"
        assert !set.is(objectUnderTest."$field")
        testValidation([], [containsNullError])

        String upperPlural = field[0].toUpperCase() + field.substring(1)
        objectUnderTest."$field" = [] as Set;
        objectUnderTest."add$upperPlural"(set)
        assert set == objectUnderTest."$field"
        testValidation([], [containsNullError])

        objectUnderTest."$field" = [] as Set;
        objectUnderTest."add$upperSingle"(value2)
        objectUnderTest."add$upperSingle"(value1)
        assert set == objectUnderTest."$field"
        testValidation([], [containsNullError])

        objectUnderTest."remove$upperSingle"(value2)
        assert ([value1] as Set) == objectUnderTest."$field"
        testValidation([], [containsNullError])

        objectUnderTest."add$upperSingle"(null);
        testValidation([containsNullError], [])
    }
}
