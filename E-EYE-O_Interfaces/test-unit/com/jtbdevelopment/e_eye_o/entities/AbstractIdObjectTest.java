package com.jtbdevelopment.e_eye_o.entities;

import com.google.common.base.Strings;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.*;

import static org.testng.Assert.*;

/**
 * Date: 12/8/12
 * Time: 6:58 PM
 */
public class AbstractIdObjectTest {
    protected static final String BLANK = "";
    protected static final String GENERALLY_ACCEPTABLE_VALUE = "A Value";
    protected static final String NULL = null;
    protected static final String TOO_LONG_FOR_NAME;
    protected static final String TOO_LONG_FOR_SHORT_NAME;
    protected static final String TOO_LONG_FOR_DESCRIPTION;
    protected static final String TOO_SHORT_FOR_LOGIN;
    protected static final String TOO_LONG_FOR_LOGIN;
    protected static final String TOO_LONG_FOR_EMAIL;
    protected static final String VALID_EMAIL = "test@test.com";
    protected static final String INVALID_EMAIL = "test";

    protected static final AppUser USER1 = new AppUserImpl().setId("USER1");
    protected static final AppUser USER2 = new AppUserImpl().setId("USER2");
    protected static final Random random = new Random();
    protected static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    protected static final Validator validator = validatorFactory.getValidator();
    protected int idCounter = 1;

    static {
        TOO_LONG_FOR_NAME = Strings.padStart("", IdObject.MAX_NAME_SIZE + 1, 'X');
        TOO_LONG_FOR_DESCRIPTION = Strings.padStart("", IdObject.MAX_DESCRIPTION_SIZE + 1, 'X');
        TOO_LONG_FOR_SHORT_NAME = Strings.padStart("", IdObject.MAX_SHORT_NAME_SIZE + 1, 'X');
        TOO_SHORT_FOR_LOGIN = Strings.padStart("", AppUser.MIN_LOGIN_SIZE - 1, 'X');
        TOO_LONG_FOR_LOGIN = Strings.padStart("", AppUser.MAX_LOGIN_SIZE + 1, 'X');
        TOO_LONG_FOR_EMAIL = Strings.padStart(VALID_EMAIL, AppUser.MAX_EMAIL_SIZE + 1, 'X');
    }

    private void validateNotExpectingError(final IdObject object, final String notAllowedViolation) {
        Set<String> notAllowed = new HashSet<>();
        notAllowed.add(notAllowedViolation);
        validateExpectingErrors(object, new HashSet<String>(), notAllowed);
    }

    private void validateExpectingError(final IdObject object, final String expectedViolation) {
        Set<String> allowed = new HashSet<>();
        allowed.add(expectedViolation);
        validateExpectingErrors(object, allowed, new HashSet<String>());
    }

    private void validateExpectingErrors(final IdObject object, final String[] allowedViolations, final String[] notAllowedViolations) {
        Set<String> allowed = new HashSet<>();
        Collections.addAll(allowed, allowedViolations);
        Set<String> notAllowed = new HashSet<>();
        Collections.addAll(notAllowed, notAllowedViolations);
        validateExpectingErrors(object, allowed, notAllowed);
    }

    private void validateExpectingErrors(final IdObject object, final Set<String> expectedViolations, final Set<String> notAllowedViolations) {
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

    protected <T extends AppUserOwnedObject> void checkDefaultAndAppUserConstructorTests(final Class<T> entityType) {
        try {
            Constructor<T> def = entityType.getConstructor();

            AppUserOwnedObject entityFromDefaultConstructor = def.newInstance();
            assertEquals(null, entityFromDefaultConstructor.getAppUser());
            validateExpectingError(entityFromDefaultConstructor, AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);

            Constructor<T> appUser = entityType.getConstructor(AppUser.class);
            final AppUserImpl user = new AppUserImpl();
            AppUserOwnedObject appUserO = appUser.newInstance(user);
            assertEquals(user, appUserO.getAppUser());
            validateNotExpectingError(user, AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends IdObject> void checkStringSetGetsAndValidateNullsAsError(final Class<T> entityType, final String attribute, final String validationError) {
        checkStringSetGetsAndValidate(entityType, attribute, true, validationError);
    }

    protected <T extends IdObject> void checkStringGetSetsAndValidateNullsAndBlanksAsError(final Class<T> entityType, final String attribute, final String validationError) {
        checkStringSetGetsAndValidate(entityType, attribute, false, validationError);
    }

    private <T extends IdObject> void checkStringSetGetsAndValidate(final Class<T> entityType, final String attribute, boolean blanksOK, final String validationError) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            T o;
            o = entityType.newInstance();
            setter.invoke(o, GENERALLY_ACCEPTABLE_VALUE);
            assertEquals(GENERALLY_ACCEPTABLE_VALUE, getter.invoke(o));
            validateNotExpectingError(o, validationError);

            o = entityType.newInstance();
            setter.invoke(o, BLANK);
            assertEquals(BLANK, getter.invoke(o));
            if (blanksOK) {
                validateNotExpectingError(o, validationError);
            } else {
                validateExpectingError(o, validationError);
            }

            o = entityType.newInstance();
            setter.invoke(o, NULL);
            assertEquals(NULL, getter.invoke(o));
            validateExpectingError(o, validationError);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends IdObject> void checkStringSizeValidation(final Class<T> entityType, final String attribute, final String tooBigValue, String sizeError) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            T o;

            o = entityType.newInstance();
            setter.invoke(o, tooBigValue);
            assertEquals(tooBigValue, getter.invoke(o));
            validateExpectingError(o, sizeError);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends IdObject> void checkStringSetGetsWithBlanksAndNullsAsException(final Class<T> entityType, final String attribute) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            T o = entityType.newInstance();
            setter.invoke(o, GENERALLY_ACCEPTABLE_VALUE);
            assertEquals(GENERALLY_ACCEPTABLE_VALUE, getter.invoke(o));
            o = entityType.newInstance();
            boolean exception = false;
            try {
                setter.invoke(o, NULL);
            } catch (InvocationTargetException i) {
                exception = i.getTargetException() instanceof InvalidParameterException;
            }
            if (!exception) {
                fail(setter.getName() + " did not throw InvalidParameterException on null value");
            }
            o = entityType.newInstance();
            exception = false;
            try {
                setter.invoke(o, BLANK);
            } catch (InvocationTargetException i) {
                exception = i.getTargetException() instanceof InvalidParameterException;
            }
            if (!exception) {
                fail(setter.getName() + " did not throw InvalidParameterException on blank value");
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends IdObject> void checkBooleanDefaultAndSetGet(final Class<T> entityType, final String attribute, final boolean defaultValue) {
        try {
            Method setter = getSetMethod(entityType, attribute, boolean.class);
            Method getter = getIsOrGetMethod(entityType, attribute);
            T o = entityType.newInstance();
            assertEquals(defaultValue, getter.invoke(o));
            setter.invoke(o, !defaultValue);
            assertEquals(!defaultValue, getter.invoke(o));
            setter.invoke(o, defaultValue);
            assertEquals(defaultValue, getter.invoke(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkGetSetIsUnmodifiable(final Collection collection) {
        collection.clear();
    }

    //  Check collection takes values but uses it's own set container
    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkSetCollection(final Class<T> entityUnderTest,
                                                                                                   final Class<C> collectionEntity,
                                                                                                   final String collectionName) {
        try {
            T entity = entityUnderTest.newInstance().setAppUser(USER1);
            Method setter = getSetMethod(entityUnderTest, collectionName, Set.class);
            Method getter = getGetMethod(entityUnderTest, collectionName);

            Set<T> resultSet = (Set<T>) getter.invoke(entity);
            assertTrue(resultSet.isEmpty());
            Set<C> newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            setter.invoke(entity, newSet);
            resultSet = (Set<T>) getter.invoke(entity);
            assertTrue(resultSet.containsAll(newSet));
            assertTrue(newSet.containsAll(resultSet));
            validator.validate(entity);

            newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            setter.invoke(entity, newSet);
            resultSet = (Set<T>) getter.invoke(entity);
            assertTrue(resultSet.containsAll(newSet));
            assertTrue(newSet.containsAll(resultSet));
            validator.validate(entity);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    //  Check collection takes values but uses it's own set container
    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkAddCollection(final Class<T> entityUnderTest,
                                                                                                   final Class<C> collectionEntity,
                                                                                                   final String collectionName) {
        try {
            T entity = entityUnderTest.newInstance().setAppUser(USER1);
            Method setter = getSetMethod(entityUnderTest, collectionName, Set.class);
            Method adder = getAddMethod(entityUnderTest, collectionName, Collection.class);
            Method getter = getGetMethod(entityUnderTest, collectionName);

            Set<T> resultSet = (Set<T>) getter.invoke(entity);
            assertTrue(resultSet.isEmpty());
            Set<C> firstSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            setter.invoke(entity, firstSet);

            Set<C> secondSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            adder.invoke(entity, secondSet);
            resultSet = (Set<T>) getter.invoke(entity);
            assertEquals(firstSet.size() + secondSet.size(), resultSet.size());
            assertTrue(resultSet.containsAll(firstSet));
            assertTrue(resultSet.containsAll(secondSet));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkSetCollectionValidates(final Class<T> entityUnderTest,
                                                                                                            final Class<C> collectionEntity,
                                                                                                            final String collectionName) {
        try {
            Method setter = getSetMethod(entityUnderTest, collectionName, Set.class);
            checkCollectionFunctionValidates(entityUnderTest, collectionEntity, setter);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkAddCollectionValidates(final Class<T> entityUnderTest,
                                                                                                            final Class<C> collectionEntity,
                                                                                                            final String collectionName) {
        try {
            Method setter = getAddMethod(entityUnderTest, collectionName, Collection.class);
            checkCollectionFunctionValidates(entityUnderTest, collectionEntity, setter);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkCollectionFunctionValidates(final Class<T> entityUnderTest, final Class<C> collectionEntity, final Method changeMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        T entity = entityUnderTest.newInstance().setAppUser(USER1);
        Set<C> newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
        changeMethod.invoke(entity, newSet);
        newSet.add(getSingleOfFor(collectionEntity, USER2));
        try {
            changeMethod.invoke(entity, newSet);
        } catch (InvocationTargetException ip) {
            if (ip.getTargetException() instanceof InvalidParameterException)
                return;
        }
        fail(entityUnderTest.getSimpleName() + " did not catch setting collection where it contains different user owned object");
    }

    protected Method getIsOrGetMethod(final Class<? extends IdObject> entityType, final String attribute) throws NoSuchMethodException {
        try {
            return entityType.getMethod("is" + capitalizeAttribute(attribute));
        } catch (NoSuchMethodException e) {
            //
        }
        return getGetMethod(entityType, attribute);
    }

    protected <T extends AppUserOwnedObject> Set<T> getSetOfFor(final Class<T> entityType, final AppUser user, final int size) {
        Set<T> result = new HashSet<>();
        for (int i = 0; i < size; ++i) {
            result.add(getSingleOfFor(entityType, user));
        }
        return result;
    }

    private <T extends AppUserOwnedObject> T getSingleOfFor(final Class<T> entityType, final AppUser user) {
        try {
            return entityType.newInstance().setAppUser(user).setId("" + (idCounter++));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    protected Method getGetMethod(final Class<? extends IdObject> entityType, final String attribute) throws NoSuchMethodException {
        return entityType.getMethod("get" + capitalizeAttribute(attribute));
    }

    protected Method getSetMethod(final Class<? extends IdObject> entityType, final String attribute, final Class paramType) throws NoSuchMethodException {
        return entityType.getMethod("set" + capitalizeAttribute(attribute), paramType);
    }

    protected Method getAddMethod(final Class<? extends IdObject> entityType, final String attribute, final Class paramType) throws NoSuchMethodException {
        return entityType.getMethod("add" + capitalizeAttribute(attribute), paramType);
    }

    protected String capitalizeAttribute(final String attribute) {
        return attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
    }
}
