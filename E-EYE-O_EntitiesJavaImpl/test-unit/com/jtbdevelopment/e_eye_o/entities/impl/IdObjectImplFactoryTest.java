package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.jmock.Mockery;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/5/13
 * Time: 11:58 AM
 */
public class IdObjectImplFactoryTest {
    public static final List<Class<AppUser>> APP_USER_CLASS_ARRAY = Arrays.asList(AppUser.class);
    public static final List<Class<?>> NOT_APP_USER_CLASSES_ARRAY = Arrays.asList(Object.class, List.class, String.class, Integer.class, Set.class, HashMap.class);
    //  TODO - dynamic
    public static final List<Class<? extends AppUserOwnedObject>> APP_USER_OWNED_CLASSES_ARRAY = Arrays.asList(ClassList.class, Student.class, DeletedObject.class, Photo.class, Observation.class, ObservationCategory.class, TwoPhaseActivity.class, AppUserSettings.class);
    private static final Mockery context = new Mockery();
    private static final AppUser appUser = context.mock(AppUser.class);
    private static final IdObjectFactoryImpl factory = new IdObjectFactoryImpl();

    @Test
    public void testNewIdObjectForKnownClasses() throws Exception {
        assertEquals(AppUserImpl.class, factory.newIdObject(AppUser.class).getClass());
        assertEquals(ClassListImpl.class, factory.newAppUserOwnedObject(ClassList.class, appUser).getClass());
        assertEquals(ObservationImpl.class, factory.newAppUserOwnedObject(Observation.class, appUser).getClass());
        assertEquals(ObservationCategoryImpl.class, factory.newAppUserOwnedObject(ObservationCategory.class, appUser).getClass());
        assertEquals(PhotoImpl.class, factory.newAppUserOwnedObject(Photo.class, appUser).getClass());
        assertEquals(StudentImpl.class, factory.newAppUserOwnedObject(Student.class, appUser).getClass());
        assertEquals(DeletedObjectImpl.class, factory.newAppUserOwnedObject(DeletedObject.class, appUser).getClass());
        assertEquals(TwoPhaseActivityImpl.class, factory.newAppUserOwnedObject(TwoPhaseActivity.class, appUser).getClass());
        assertEquals(AppUserSettingsImpl.class, factory.newAppUserOwnedObject(AppUserSettings.class, appUser).getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewIdObjectForAppUserOwnedObjects() {
        for (Class c : APP_USER_OWNED_CLASSES_ARRAY) {
            assertNull(((AppUserOwnedObject) factory.newIdObject(c)).getAppUser());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewIdObjectForUnknownClasses() throws Exception {
        boolean exception;
        for (Class c : NOT_APP_USER_CLASSES_ARRAY) {
            exception = false;
            try {
                factory.newIdObject(c);
            } catch (IllegalArgumentException e) {
                exception = true;
                assertEquals("Unknown class type " + c.getSimpleName(), e.getMessage());
            }
            assertTrue("Expected exception for " + c.getSimpleName(), exception);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewAppUserObjectForNonAppUserOwnedObjects() {
        boolean exception;
        for (Class c : Arrays.asList(AppUser.class)) {
            exception = false;
            try {
                factory.newAppUserOwnedObject(c, null);
            } catch (IllegalArgumentException e) {
                exception = true;
                assertEquals("You cannot use this method to create non-app user owned objects.", e.getMessage());
            }
            assertTrue("Expected exception for " + c.getSimpleName(), exception);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewAppUserObjectForUnknownClasses() throws Exception {
        boolean exception;
        for (Class c : NOT_APP_USER_CLASSES_ARRAY) {
            exception = false;
            try {
                factory.newAppUserOwnedObject(c, null);
            } catch (IllegalArgumentException e) {
                exception = true;
                assertEquals("You cannot use this method to create non-app user owned objects.", e.getMessage());
            }
            assertTrue("Expected exception for " + c.getSimpleName(), exception);
        }
    }


    @Test
    public void testNewAppUser() throws Exception {
        assertEquals(AppUserImpl.class, factory.newAppUser().getClass());
    }

    @Test
    public void testNewClassListWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newClassList(appUser);
        assertEquals(ClassListImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewObservationWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newObservation(appUser);
        assertEquals(ObservationImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewObservationCategoryWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newObservationCategory(appUser);
        assertEquals(ObservationCategoryImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewPhotoWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newPhoto(appUser);
        assertEquals(PhotoImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewStudentWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newStudent(appUser);
        assertEquals(StudentImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }
}
