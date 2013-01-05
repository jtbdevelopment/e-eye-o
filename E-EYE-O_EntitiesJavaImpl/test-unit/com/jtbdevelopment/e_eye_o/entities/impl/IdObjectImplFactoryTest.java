package com.jtbdevelopment.e_eye_o.entities.impl;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.jmock.Mockery;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Date: 1/5/13
 * Time: 11:58 AM
 */
public class IdObjectImplFactoryTest {
    private static final Mockery context = new Mockery();
    private static final AppUser appUser = context.mock(AppUser.class);
    private static final IdObjectImplFactory factory = new IdObjectImplFactory();

    @Test
    public void testNewIdObjectForKnownClasses() throws Exception {
        assertEquals(AppUserImpl.class, factory.newIdObject(AppUser.class).getClass());
        assertEquals(ClassListImpl.class, factory.newIdObject(ClassList.class).getClass());
        assertEquals(ObservationImpl.class, factory.newIdObject(Observation.class).getClass());
        assertEquals(ObservationCategoryImpl.class, factory.newIdObject(ObservationCategory.class).getClass());
        assertEquals(PhotoImpl.class, factory.newIdObject(Photo.class).getClass());
        assertEquals(StudentImpl.class, factory.newIdObject(Student.class).getClass());
    }

    @Test
    public void testNewIdObjectForUnknownClasses() throws Exception {
        boolean exception;
        for(Class c :Arrays.asList(Object.class, List.class, String.class, Integer.class, Set.class, HashMap.class)) {
            exception = false;
            try {
                factory.newIdObject(c);
            } catch ( IllegalArgumentException e ) {
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
    public void testNewClassList() throws Exception {
        assertEquals(ClassListImpl.class, factory.newClassList().getClass());
    }

    @Test
    public void testNewClassListWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newClassList(appUser);
        assertEquals(ClassListImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewObservation() throws Exception {
        assertEquals(ObservationImpl.class, factory.newObservation().getClass());
    }

    @Test
    public void testNewObservationWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newObservation(appUser);
        assertEquals(ObservationImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewObservationCategory() throws Exception {
        assertEquals(ObservationCategoryImpl.class, factory.newObservationCategory().getClass());
    }

    @Test
    public void testNewObservationCategoryWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newObservationCategory(appUser);
        assertEquals(ObservationCategoryImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewPhoto() throws Exception {
        assertEquals(PhotoImpl.class, factory.newPhoto().getClass());
    }

    @Test
    public void testNewPhotoWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newPhoto(appUser);
        assertEquals(PhotoImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }

    @Test
    public void testNewStudent() throws Exception {
        assertEquals(StudentImpl.class, factory.newStudent().getClass());
    }

    @Test
    public void testNewStudentWAppUser() throws Exception {
        final AppUserOwnedObject object = factory.newStudent(appUser);
        assertEquals(StudentImpl.class, object.getClass());
        assertEquals(appUser, object.getAppUser());
    }
}
