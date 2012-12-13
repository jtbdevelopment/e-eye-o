package com.jtbdevelopment.e_eye_o.entities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Date: 12/8/12
 * Time: 6:58 PM
 */
public class AbstractIdObjectTest {
    protected static final String BLANK = "";
    protected static final String VALUE = "A Value";
    protected static final String NULL = null;
    protected static final AppUser USER1 = new AppUserImpl().setId("USER1");
    protected static final AppUser USER2 = new AppUserImpl().setId("USER2");
    protected static final Random random = new Random();
    protected int idCounter = 1;


    protected <T extends AppUserOwnedObject> void checkDefaultAndAppUserConstructorTests(final Class<T> entityType) {
        try {
            Constructor<T> def = entityType.getConstructor();

            AppUserOwnedObject defO = def.newInstance();
            assertEquals(null, defO.getAppUser());

            Constructor<T> appUser = entityType.getConstructor(AppUser.class);
            final AppUserImpl user = new AppUserImpl();
            AppUserOwnedObject appUserO = appUser.newInstance(user);
            assertEquals(user, appUserO.getAppUser());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends IdObject> void checkStringSetGetsWithNullsSavedAsBlanks(final Class<T> entityType, final String attribute) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            T o = entityType.newInstance();
            setter.invoke(o, BLANK);
            assertEquals(BLANK, getter.invoke(o));
            o = entityType.newInstance();
            setter.invoke(o, VALUE);
            assertEquals(VALUE, getter.invoke(o));
            o = entityType.newInstance();
            setter.invoke(o, NULL);
            assertEquals(BLANK, getter.invoke(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends IdObject> void checkStringSetGetsWithBlanksAndNullsAsException(final Class<T> entityType, final String attribute) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            T o = entityType.newInstance();
            setter.invoke(o, VALUE);
            assertEquals(VALUE, getter.invoke(o));
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

            newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            setter.invoke(entity, newSet);
            resultSet = (Set<T>) getter.invoke(entity);
            assertTrue(resultSet.containsAll(newSet));
            assertTrue(newSet.containsAll(resultSet));
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
