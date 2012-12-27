package com.jtbdevelopment.e_eye_o.entities;

import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Date: 12/8/12
 * Time: 6:58 PM
 */
public class AbstractAppUserOwnedObjectTest<T extends AppUserOwnedObject> extends AbstractIdObjectTest<T> {

    protected static final AppUser USER1 = new AppUserImpl().setId("USER1");
    protected static final AppUser USER2 = new AppUserImpl().setId("USER2");

    protected AbstractAppUserOwnedObjectTest(Class<T> entityUnderTest) {
        super(entityUnderTest);
    }

    protected void checkDefaultAndAppUserConstructorTests() {
        try {
            if (AppUserOwnedObject.class.isAssignableFrom(entityUnderTest)) {
                Constructor<? extends AppUserOwnedObject> def = entityUnderTest.getConstructor();
                AppUserOwnedObject entityFromDefaultConstructor = def.newInstance();
                assertEquals(null, entityFromDefaultConstructor.getAppUser());
                validateExpectingError(entityFromDefaultConstructor, AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);

                Constructor<? extends AppUserOwnedObject> appUser = entityUnderTest.getConstructor(AppUser.class);
                final AppUserImpl user = new AppUserImpl();
                AppUserOwnedObject appUserO = appUser.newInstance(user);
                assertEquals(user, appUserO.getAppUser());
                validateNotExpectingError(user, AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);
            } else {
                throw new InvalidParameterException("Cannot perform app user owned constructor tests on non-appuser owned entity " + entityUnderTest.getSimpleName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void checkGetSetIsUnmodifiable(final Collection collection) {
        collection.clear();
    }

    //  Check collection takes values but uses it's own set container
    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkSetCollection(final Class<C> collectionEntity,
                                                                                                   final String collectionName) {
        try {
            T entity = entityUnderTest.newInstance().setAppUser(USER1);
            Method setter = getSetMethod(collectionName, Set.class);
            Method getter = getGetMethod(collectionName);

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
    protected <C extends AppUserOwnedObject> void checkAddCollection(                                                                                                   final Class<C> collectionEntity,
                                                                                                   final String collectionName) {
        try {
            T entity = entityUnderTest.newInstance().setAppUser(USER1);
            Method setter = getSetMethod(collectionName, Set.class);
            Method adder = getAddMethod(collectionName, Collection.class);
            Method getter = getGetMethod(collectionName);

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

    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkSetCollectionValidates( final Class<C> collectionEntity,
                                                                                                            final String collectionName) {
        try {
            Method setter = getSetMethod(collectionName, Set.class);
            checkCollectionFunctionValidates(collectionEntity, setter);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T extends AppUserOwnedObject, C extends AppUserOwnedObject> void checkAddCollectionValidates(final Class<C> collectionEntity,
                                                                                                            final String collectionName) {
        try {
            Method setter = getAddMethod(collectionName, Collection.class);
            checkCollectionFunctionValidates(collectionEntity, setter);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private <C extends AppUserOwnedObject> void checkCollectionFunctionValidates(final Class<C> collectionEntity, final Method changeMethod) throws InstantiationException, IllegalAccessException, InvocationTargetException {
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

    protected Method getAddMethod(final String attribute, final Class paramType) throws NoSuchMethodException {
        return entityUnderTest.getMethod("add" + StringUtils.capitalize(attribute), paramType);
    }

}
