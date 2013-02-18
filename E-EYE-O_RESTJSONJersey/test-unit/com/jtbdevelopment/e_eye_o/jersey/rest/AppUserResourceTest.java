package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Date: 2/18/13
 * Time: 4:33 PM
 */
public class AppUserResourceTest extends AbstractResourceTest {
    private static String userId = "user-id";
    private AppUser user;
    final Set<AppUserOwnedObject> objects = new HashSet<>();
    final String output = "some-json";

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        user = context.mock(AppUser.class);
        context.checking(new Expectations(){{
            allowing(dao).get(AppUser.class, userId);
            will(returnValue(user));
            allowing(serializer).write(objects);
            will(returnValue(output));
        }});
    }

    @Test
    public void testGetEntitiesForUserAnnotations() throws NoSuchMethodException {
        checkGETJSONForMethod(AppUserResource.class, "getEntitiesForUser");
    }

    @Test
    public void testGetEntitiesForUser() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(AppUserOwnedObject.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, resource.getEntitiesForUser());
    }

    @Test
    public void testGetArchivedAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getArchived", "archived");
    }

    @Test
    public void testGetArchived() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource archived = resource.getArchived();
        assertNotNull(archived);
        context.checking(new Expectations() {{
            one(dao).getArchivedEntitiesForUser(AppUserOwnedObject.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Student.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, archived.getEntitiesForUser());
        assertEquals(output, archived.getPhotos().getEntitiesForUser());
        assertEquals(output, archived.getStudents().getEntitiesForUser());
    }

    @Test
    public void testGetActiveAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getActive", "active");
    }

    @Test
    public void testGetActive() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource active = resource.getActive();
        assertNotNull(active);
        context.checking(new Expectations() {{
            one(dao).getActiveEntitiesForUser(AppUserOwnedObject.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, active.getEntitiesForUser());
        assertEquals(output, active.getClassLists().getEntitiesForUser());
        assertEquals(output, active.getObservations().getEntitiesForUser());
    }

    @Test
    public void testMultipleLevelsOfActiveArchivedNulls() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource active = resource.getActive();
        assertNotNull(active);
        assertNull(active.getActive());
        assertNull(active.getArchived());
        AppUserResource archived = resource.getArchived();
        assertNotNull(archived);
        assertNull(archived.getActive());
        assertNull(archived.getArchived());
    }

    @Test
    public void testGetPhotosAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getPhotos", "photos");
    }

    @Test
    public void testGetPhotos() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource photos = resource.getPhotos();
        assertNotNull(photos);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, photos.getEntitiesForUser());
        assertEquals(output, photos.getActive().getEntitiesForUser());
        assertEquals(output, photos.getArchived().getEntitiesForUser());
    }

    @Test
    public void testGetStudentsAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getStudents", "students");
    }

    @Test
    public void testGetStudents() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource students = resource.getStudents();
        assertNotNull(students);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(Student.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Student.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Student.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, students.getEntitiesForUser());
        assertEquals(output, students.getActive().getEntitiesForUser());
        assertEquals(output, students.getArchived().getEntitiesForUser());
    }

    @Test
    public void testGetClassListsAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getClassLists", "classlists");
    }

    @Test
    public void testGetClassLists() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource classLists = resource.getClassLists();
        assertNotNull(classLists);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, classLists.getEntitiesForUser());
        assertEquals(output, classLists.getActive().getEntitiesForUser());
        assertEquals(output, classLists.getArchived().getEntitiesForUser());
    }

    @Test
    public void testGetObservationsAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getObservations", "observations");
    }


    @Test
    public void testGetObservations() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource observations = resource.getObservations();
        assertNotNull(observations);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, observations.getEntitiesForUser());
        assertEquals(output, observations.getActive().getEntitiesForUser());
        assertEquals(output, observations.getArchived().getEntitiesForUser());
    }

    @Test
    public void testGetObservationCategoriesAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getObservationCategories", "categories");
    }

    @Test
    public void testGetObservationCategories() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource categories = resource.getObservationCategories();
        assertNotNull(categories);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, categories.getEntitiesForUser());
        assertEquals(output, categories.getActive().getEntitiesForUser());
        assertEquals(output, categories.getArchived().getEntitiesForUser());
    }

    @Test
    public void testGettingMultipleEntityTypeDepthsNulls() throws Exception {
        // Not testing every permutation
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        assertNull(resource.getClassLists().getObservations());
        assertNull(resource.getObservations().getStudents());
        assertNull(resource.getPhotos().getStudents());
        assertNull(resource.getStudents().getClassLists());
        assertNull(resource.getObservationCategories().getClassLists());
    }

    @Test
    public void testGetEntityResourceAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getAppUserEntityResource", "{entityId}");
    }

    @Test
    public void testGetAppUserEntityResource() throws Exception {
        final String someEntityId = "entity";
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserEntityResource entityResource = resource.getAppUserEntityResource(someEntityId);
        assertNotNull(entityResource);
    }
}
