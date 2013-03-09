package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.validation.ConsistentAppUserValidator;
import org.apache.commons.beanutils.PropertyUtils;
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

    protected static final AppUser USER1;
    protected static final AppUser USER2;

    static {
        USER1 = new AppUserImpl();
        USER1.setId("USER1");
        USER2 = new AppUserImpl();
        USER2.setId("USER2");
    }

    public static final String ADD = "add";
    public static final String REMOVE = "remove";
    public static final String IMPL = "Impl";

    protected AbstractAppUserOwnedObjectTest(Class<T> entityUnderTest) {
        super(entityUnderTest);
    }

    @Override
    protected T newDefaultInstance() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        return newDefaultInstance(entityUnderTest);
    }

    protected <T extends AppUserOwnedObject> T newDefaultInstance(final Class<T> entityType) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<T> constructor = entityType.getDeclaredConstructor(AppUser.class);
        constructor.setAccessible(true);
        return constructor.newInstance((AppUser) null);
    }

    protected void checkDefaultAndAppUserConstructorTests() {
        try {
            if (AppUserOwnedObject.class.isAssignableFrom(entityUnderTest)) {
                AppUserOwnedObject entityFromDefaultConstructor = newDefaultInstance();
                assertEquals(null, entityFromDefaultConstructor.getAppUser());
                validateExpectingError(entityFromDefaultConstructor, AppUserOwnedObject.APP_USER_CANNOT_BE_NULL_ERROR);

                Constructor<T> appUser = entityUnderTest.getDeclaredConstructor(AppUser.class);
                appUser.setAccessible(true);
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

    protected void checkCollectionIsUnmodifiable(final Collection collection) {
        try {
            collection.clear();
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail("Should have thrown exception on clear");
    }

    @SuppressWarnings("unchecked")
    protected <C extends AppUserOwnedObject> void checkSetCollection(final Class<C> collectionEntity,
                                                                     final String collectionName,
                                                                     final String nullValueError
    ) {
        try {
            T entity = newDefaultInstance();
            entity.setAppUser(USER1);
            Set<C> resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertTrue(resultSet.isEmpty());

            Set<C> newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            PropertyUtils.setProperty(entity, collectionName, newSet);
            resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertTrue(resultSet.containsAll(newSet));
            assertTrue(newSet.containsAll(resultSet));
            validateNotExpectingErrors(entity, new String[]{nullValueError, ConsistentAppUserValidator.getGeneralErrorMessage(entity)});

            newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            PropertyUtils.setProperty(entity, collectionName, newSet);
            resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertTrue(resultSet.containsAll(newSet));
            assertTrue(newSet.containsAll(resultSet));
            validateNotExpectingErrors(entity, new String[]{nullValueError, ConsistentAppUserValidator.getGeneralErrorMessage(entity)});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <C extends AppUserOwnedObject> void checkAddManyEntitiesToCollection(final Class<C> collectionEntity,
                                                                                   final String collectionName,
                                                                                   final String nullValueError) {
        try {
            Method adder = getAddMethod(collectionName, Collection.class);

            T entity = newDefaultInstance();
            entity.setAppUser(USER1);
            Set<C> resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertTrue(resultSet.isEmpty());
            Set<C> firstSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            PropertyUtils.setSimpleProperty(entity, collectionName, firstSet);

            Set<C> secondSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));
            adder.invoke(entity, secondSet);
            resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertEquals(firstSet.size() + secondSet.size(), resultSet.size());
            assertTrue(resultSet.containsAll(firstSet));
            assertTrue(resultSet.containsAll(secondSet));
            validateNotExpectingErrors(entity, new String[]{nullValueError, ConsistentAppUserValidator.getGeneralErrorMessage(entity)});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <C extends AppUserOwnedObject> void checkAddSingleEntityToCollection(final Class<C> collectionEntity,
                                                                                   final String singleName,
                                                                                   final String collectionName,
                                                                                   final String nullValueError) {
        try {
            Method adder = getAddMethod(singleName, collectionEntity);

            T entity = newDefaultInstance();
            entity.setAppUser(USER1);

            C ownedObject = getSingleOfFor(collectionEntity, USER1);
            adder.invoke(entity, ownedObject);
            Set<C> resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertEquals(1, resultSet.size());
            assertTrue(resultSet.contains(ownedObject));
            validateNotExpectingErrors(entity, new String[]{nullValueError, ConsistentAppUserValidator.getGeneralErrorMessage(entity)});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <C extends AppUserOwnedObject> void checkRemoveSingleEntityToCollection(final Class<C> collectionEntity,
                                                                                      final String singleName,
                                                                                      final String collectionName,
                                                                                      final String nullValueError) {
        try {
            Method adder = getAddMethod(singleName, collectionEntity);
            Method remover = getRemoveMethod(singleName, collectionEntity);

            T entity = newDefaultInstance();
            entity.setAppUser(USER1);

            C ownedObject1 = getSingleOfFor(collectionEntity, USER1);
            C ownedObject2 = getSingleOfFor(collectionEntity, USER1);
            adder.invoke(entity, ownedObject1);
            adder.invoke(entity, ownedObject2);
            Set<C> resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertEquals(2, resultSet.size());
            assertTrue(resultSet.contains(ownedObject1));
            assertTrue(resultSet.contains(ownedObject2));

            remover.invoke(entity, ownedObject1);
            resultSet = (Set<C>) PropertyUtils.getSimpleProperty(entity, collectionName);
            assertEquals(1, resultSet.size());
            assertTrue(resultSet.contains(ownedObject2));
            validateNotExpectingErrors(entity, new String[]{nullValueError, ConsistentAppUserValidator.getGeneralErrorMessage(entity)});
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected <C extends AppUserOwnedObject> void checkCollectionValidates(final Class<C> collectionEntity,
                                                                           final String collectionName,
                                                                           final String nullValueError) {
        try {
            T entity = newDefaultInstance();
            entity.setAppUser(USER1);

            Set<C> newSet = getSetOfFor(collectionEntity, USER1, random.nextInt(5));

            final C misMatchObject = getSingleOfFor(collectionEntity, USER2);
            newSet.add(misMatchObject);

            newSet.add(null);

            PropertyUtils.setSimpleProperty(entity, collectionName, newSet);
            validateExpectingErrors(entity, new String[]{nullValueError, ConsistentAppUserValidator.getGeneralErrorMessage(entity), ConsistentAppUserValidator.getSpecificErrorMessage(entity, misMatchObject)});

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected <C extends AppUserOwnedObject> Set<C> getSetOfFor(final Class<C> entityType, final AppUser user, final int size) {
        Set<C> result = new HashSet<>();
        for (int i = 0; i < size; ++i) {
            result.add(getSingleOfFor(entityType, user));
        }
        return result;
    }

    private <C extends AppUserOwnedObject> C getSingleOfFor(final Class<C> entityType, final AppUser user) {
        try {
            final C entity = newDefaultInstance(entityType);
            entity.setAppUser(user);
            entity.setId("" + (idCounter++));
            return entity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected Method getAddMethod(final String attribute, final Class paramType) throws NoSuchMethodException {
        if (paramType.isInterface() || Collection.class.isAssignableFrom(paramType)) {
            return entityUnderTest.getMethod(ADD + StringUtils.capitalize(attribute), paramType);
        } else {
            Class[] interfaces = paramType.getInterfaces();
            String lookingFor = paramType.getSimpleName().replace(IMPL, BLANK);
            for (Class iface : interfaces) {
                if (iface.getSimpleName().equals(lookingFor)) {
                    return entityUnderTest.getMethod(ADD + StringUtils.capitalize(attribute), iface);
                }
            }
            throw new RuntimeException("Unable to find addMethod with paramType " + paramType.getSimpleName());
        }
    }

    protected Method getRemoveMethod(final String attribute, final Class paramType) throws NoSuchMethodException {
        if (paramType.isInterface() || Collection.class.isAssignableFrom(paramType)) {
            return entityUnderTest.getMethod(REMOVE + StringUtils.capitalize(attribute), paramType);
        } else {
            Class[] interfaces = paramType.getInterfaces();
            String lookingFor = paramType.getSimpleName().replace(IMPL, BLANK);
            for (Class iface : interfaces) {
                if (iface.getSimpleName().equals(lookingFor)) {
                    return entityUnderTest.getMethod(REMOVE + StringUtils.capitalize(attribute), iface);
                }
            }
            throw new RuntimeException("Unable to find addMethod with paramType " + paramType.getSimpleName());
        }
    }
}
