package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.jmock.Mockery;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/5/13
 * Time: 11:58 AM
 */
public class IdObjectImplFactoryTest {
    private static final Mockery context = new Mockery();
    private static final AppUser appUser = context.mock(AppUser.class);
    private static final IdObjectImplFactory factory = new IdObjectImplFactory();

    @Test
    public void testInterfaceMap() {
        Map<Class<? extends IdObject>, Class<? extends IdObject>> map = factory.implementationsForInterfaces();
        assertEquals(7, map.size());
        assertEquals(AppUserImpl.class, map.get(AppUser.class));
        assertEquals(ClassListImpl.class, map.get(ClassList.class));
        assertEquals(ObservationImpl.class, map.get(Observation.class));
        assertEquals(ObservationCategoryImpl.class, map.get(ObservationCategory.class));
        assertEquals(PhotoImpl.class, map.get(Photo.class));
        assertEquals(StudentImpl.class, map.get(Student.class));
        assertEquals(DeletedObjectImpl.class, map.get(DeletedObject.class));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testInterfaceMapIsUnmodifiable() {
        Map<Class<? extends IdObject>, Class<? extends IdObject>> map = factory.implementationsForInterfaces();
        map.clear();
    }


    @Test
    public void testImplementationForInterface() throws Exception {
        assertEquals(AppUserImpl.class, factory.implementationForInterface(AppUser.class));
        assertEquals(ClassListImpl.class, factory.implementationForInterface(ClassList.class));
        assertEquals(ObservationImpl.class, factory.implementationForInterface(Observation.class));
        assertEquals(ObservationCategoryImpl.class, factory.implementationForInterface(ObservationCategory.class));
        assertEquals(PhotoImpl.class, factory.implementationForInterface(Photo.class));
        assertEquals(StudentImpl.class, factory.implementationForInterface(Student.class));
        assertEquals(DeletedObjectImpl.class, factory.implementationForInterface(DeletedObject.class));
    }


    @Test
    public void testNewIdObjectForKnownClasses() throws Exception {
        assertEquals(AppUserImpl.class, factory.newIdObject(AppUser.class).getClass());
        assertEquals(ClassListImpl.class, factory.newAppUserOwnedObject(ClassList.class, appUser).getClass());
        assertEquals(ObservationImpl.class, factory.newAppUserOwnedObject(Observation.class, appUser).getClass());
        assertEquals(ObservationCategoryImpl.class, factory.newAppUserOwnedObject(ObservationCategory.class, appUser).getClass());
        assertEquals(PhotoImpl.class, factory.newAppUserOwnedObject(Photo.class, appUser).getClass());
        assertEquals(StudentImpl.class, factory.newAppUserOwnedObject(Student.class, appUser).getClass());
        assertEquals(DeletedObjectImpl.class, factory.newAppUserOwnedObject(DeletedObject.class, appUser).getClass());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewIdObjectForAppUserOwnedObjects() {
        for (Class c : Arrays.asList(ClassList.class, Student.class, DeletedObject.class, Photo.class, Observation.class, ObservationCategory.class)) {
            assertNull(((AppUserOwnedObject) factory.newIdObject(c)).getAppUser());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNewIdObjectForUnknownClasses() throws Exception {
        boolean exception;
        for (Class c : Arrays.asList(Object.class, List.class, String.class, Integer.class, Set.class, HashMap.class)) {
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
        for (Class c : Arrays.asList(Object.class, List.class, String.class, Integer.class, Set.class, HashMap.class)) {
            exception = false;
            try {
                factory.newAppUserOwnedObject(c, null);
            } catch (IllegalArgumentException e) {
                exception = true;
                assertEquals("Unknown class type " + c.getSimpleName(), e.getMessage());
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
