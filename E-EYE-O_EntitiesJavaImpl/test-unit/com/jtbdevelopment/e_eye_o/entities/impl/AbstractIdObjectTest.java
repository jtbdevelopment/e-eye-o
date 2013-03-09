package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Strings;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Date: 12/26/12
 * Time: 7:27 PM
 */
@Test
public class AbstractIdObjectTest<T extends IdObject> {
    protected static final String BLANK = "";
    protected static final String GENERALLY_ACCEPTABLE_VALUE = "A Value";
    protected static final String NULL = null;
    protected static final String TOO_LONG_FOR_NAME;
    protected static final String TOO_LONG_FOR_SHORT_NAME;
    protected static final String TOO_LONG_FOR_DESCRIPTION;
    protected static final String TOO_LONG_FOR_EMAIL;
    protected static final String VALID_EMAIL = "test@test.com";
    protected static final String INVALID_EMAIL = "test";
    protected static final Random random = new Random();
    protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    protected static final Validator validator = validatorFactory.getValidator();
    protected final Class<T> entityUnderTest;
    protected int idCounter = 1;

    static {
        TOO_LONG_FOR_NAME = Strings.padStart("", IdObject.MAX_NAME_SIZE + 1, 'X');
        TOO_LONG_FOR_DESCRIPTION = Strings.padStart("", IdObject.MAX_DESCRIPTION_SIZE + 1, 'X');
        TOO_LONG_FOR_SHORT_NAME = Strings.padStart("", IdObject.MAX_SHORT_NAME_SIZE + 1, 'X');
        TOO_LONG_FOR_EMAIL = Strings.padStart(VALID_EMAIL, AppUser.MAX_EMAIL_SIZE + 1, 'X');
    }

    protected AbstractIdObjectTest(Class<T> entityUnderTest) {
        this.entityUnderTest = entityUnderTest;
    }

    protected T newDefaultInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        return entityUnderTest.newInstance();
    }

    protected void checkStringSetGetsAndValidateNullsAsError(final String attribute, final String validationError) {
        checkStringSetGetsAndValidate(attribute, true, validationError);
    }

    protected void checkStringSetGetsAndValidateNullsAndBlanksAsError(final String attribute, final String validationError) {
        checkStringSetGetsAndValidate(attribute, false, validationError);
    }

    private void checkStringSetGetsAndValidate(final String attribute, boolean blanksOK, final String validationError) {
        try {
            checkStringSetGetValidateSingleValue(attribute, GENERALLY_ACCEPTABLE_VALUE, false, validationError);
            checkStringSetGetValidateSingleValue(attribute, BLANK, !blanksOK, validationError);
            checkStringSetGetValidateSingleValue(attribute, NULL, true, validationError);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkStringSizeValidation(final String attribute, final String tooBigValue, String sizeError) {
        try {
            checkStringSetGetValidateSingleValue(attribute, tooBigValue, true, sizeError);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkStringSetGetValidateSingleValue(final String attribute, final String value, final boolean expectingError, final String validationError) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        T o = newDefaultInstance();
        PropertyUtils.setSimpleProperty(o, attribute, value);
        assertEquals(value, PropertyUtils.getSimpleProperty(o, attribute));
        if (!expectingError) {
            validateNotExpectingError(o, validationError);
        } else {
            validateExpectingError(o, validationError);
        }
    }

    protected void validateNotExpectingError(final IdObject object, final String notAllowedViolation) {
        Set<String> notAllowed = new HashSet<>();
        notAllowed.add(notAllowedViolation);
        validateExpectingErrors(object, new HashSet<String>(), notAllowed);
    }

    protected void validateNotExpectingErrors(final IdObject object, final String[] notAllowedViolations) {
        Set<String> notAllowed = new HashSet<>();
        Collections.addAll(notAllowed, notAllowedViolations);
        validateExpectingErrors(object, new HashSet<String>(), notAllowed);
    }

    protected void validateExpectingError(final IdObject object, final String expectedViolation) {
        Set<String> allowed = new HashSet<>();
        allowed.add(expectedViolation);
        validateExpectingErrors(object, allowed, new HashSet<String>());
    }

    protected void validateExpectingErrors(final IdObject object, final String[] expectedViolations) {
        Set<String> allowed = new HashSet<>();
        Collections.addAll(allowed, expectedViolations);
        validateExpectingErrors(object, allowed, new HashSet<String>());
    }

    protected void validateExpectingErrors(final IdObject object, final Set<String> expectedViolations, final Set<String> notAllowedViolations) {
        Set<ConstraintViolation<IdObject>> allViolations = validator.validate(object);

        Set<String> expectedViolationsWorkingCopy = new HashSet<>(expectedViolations);
        Set<ConstraintViolation<IdObject>> illegalExceptions = new HashSet<>();
        for (ConstraintViolation<IdObject> violation : allViolations) {
            final String message = violation.getMessage();
            if (notAllowedViolations.contains(message)) {
                illegalExceptions.add(violation);
            } else if (expectedViolationsWorkingCopy.contains(message)) {
                expectedViolationsWorkingCopy.remove(message);
            }
        }
        assertTrue(expectedViolationsWorkingCopy.isEmpty(), "Not all expected violations were found [" + expectedViolationsWorkingCopy.toArray().toString() + "]");
        assertTrue(illegalExceptions.isEmpty(), "Unexpected violations were not empty [" + illegalExceptions.toArray().toString() + "]");
    }

    protected void checkBooleanDefaultAndSetGet(final String attribute, final boolean defaultValue) {
        try {
            T o = newDefaultInstance();
            assertEquals(defaultValue, PropertyUtils.getSimpleProperty(o, attribute));
            PropertyUtils.setSimpleProperty(o, attribute, !defaultValue);
            assertEquals(!defaultValue, PropertyUtils.getSimpleProperty(o, attribute));
            PropertyUtils.setSimpleProperty(o, attribute, defaultValue);
            assertEquals(defaultValue, PropertyUtils.getSimpleProperty(o, attribute));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
