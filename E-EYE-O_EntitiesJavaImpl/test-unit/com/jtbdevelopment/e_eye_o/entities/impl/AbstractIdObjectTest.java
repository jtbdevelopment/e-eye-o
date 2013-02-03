package com.jtbdevelopment.e_eye_o.entities.impl;

import com.google.common.base.Strings;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.impl.reflection.IdObjectInterfaceResolverImpl;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    protected static final IdObjectInterfaceResolverImpl resolver = new IdObjectInterfaceResolverImpl();
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

    protected void checkStringSetGetsAndValidateNullsAsError(final String attribute, final String validationError) {
        checkStringSetGetsAndValidate(attribute, true, validationError);
    }

    protected void checkStringSetGetsAndValidateNullsAndBlanksAsError(final String attribute, final String validationError) {
        checkStringSetGetsAndValidate(attribute, false, validationError);
    }

    private void checkStringSetGetsAndValidate(final String attribute, boolean blanksOK, final String validationError) {
        try {
            Method setter = getSetMethod(attribute, String.class);
            Method getter = getIsOrGetMethod(attribute);
            checkStringSetGetValidateSingleValue(getter, setter, GENERALLY_ACCEPTABLE_VALUE, false, validationError);
            checkStringSetGetValidateSingleValue(getter, setter, BLANK, !blanksOK, validationError);
            checkStringSetGetValidateSingleValue(getter, setter, NULL, true, validationError);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkStringSizeValidation(final String attribute, final String tooBigValue, String sizeError) {
        try {
            Method setter = getSetMethod(attribute, String.class);
            Method getter = getIsOrGetMethod(attribute);
            checkStringSetGetValidateSingleValue(getter, setter, tooBigValue, true, sizeError);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkStringSetGetValidateSingleValue(final Method getter, final Method setter, final String value, final boolean expectingError, final String validationError) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T o;
        o = entityUnderTest.newInstance();
        setter.invoke(o, value);
        assertEquals(value, getter.invoke(o));
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
            Method setter = getSetMethod(attribute, boolean.class);
            Method getter = getIsOrGetMethod(attribute);
            T o = entityUnderTest.newInstance();
            assertEquals(defaultValue, getter.invoke(o));
            setter.invoke(o, !defaultValue);
            assertEquals(!defaultValue, getter.invoke(o));
            setter.invoke(o, defaultValue);
            assertEquals(defaultValue, getter.invoke(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Method getIsOrGetMethod(final String attribute) throws NoSuchMethodException {
        return resolver.getIsOrGetMethod(entityUnderTest, attribute);
    }

    protected Method getSetMethod(final String attribute, final Class paramType) throws NoSuchMethodException {
        return resolver.getSetMethod(entityUnderTest, attribute, paramType);
    }
}
