package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.*;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
        context.checking(new Expectations() {{
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
        assertEquals(output, resource.getEntitiesForUser().getEntity());
    }

    @Test
    public void testGetArchivedAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getArchived", "archived");
    }

    @Test
    public void testGetArchived() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource archived = (AppUserResource) resource.getArchived();
        assertNotNull(archived);
        context.checking(new Expectations() {{
            one(dao).getArchivedEntitiesForUser(AppUserOwnedObject.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Student.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, archived.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) archived.getPhotos()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) archived.getStudents()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testGetActiveAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getActive", "active");
    }

    @Test
    public void testGetActive() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource active = (AppUserResource) resource.getActive();
        assertNotNull(active);
        context.checking(new Expectations() {{
            one(dao).getActiveEntitiesForUser(AppUserOwnedObject.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, active.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) active.getClassLists()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) active.getObservations()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testMultipleLevelsOfActiveArchivedNulls() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource active = (AppUserResource) resource.getActive();
        assertNotNull(active);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) active.getActive()).getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) active.getArchived()).getStatus());
        AppUserResource archived = (AppUserResource) resource.getArchived();
        assertNotNull(archived);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) archived.getActive()).getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) archived.getArchived()).getStatus());
    }

    @Test
    public void testGetPhotosAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getPhotos", "photos");
    }

    @Test
    public void testGetPhotos() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource photos = (AppUserResource) resource.getPhotos();
        assertNotNull(photos);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Photo.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, photos.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) photos.getActive()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) photos.getArchived()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testGetStudentsAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getStudents", "students");
    }

    @Test
    public void testGetStudents() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource students = (AppUserResource) resource.getStudents();
        assertNotNull(students);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(Student.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Student.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Student.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, students.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) students.getActive()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) students.getArchived()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testGetClassListsAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getClassLists", "classes");
    }

    @Test
    public void testGetClassLists() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource classLists = (AppUserResource) resource.getClassLists();
        assertNotNull(classLists);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(ClassList.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, classLists.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) classLists.getActive()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) classLists.getArchived()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testGetObservationsAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getObservations", "observations");
    }


    @Test
    public void testGetObservations() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource observations = (AppUserResource) resource.getObservations();
        assertNotNull(observations);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(Observation.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, observations.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) observations.getActive()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) observations.getArchived()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testGetObservationCategoriesAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getObservationCategories", "categories");
    }

    @Test
    public void testGetObservationCategories() throws Exception {
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserResource categories = (AppUserResource) resource.getObservationCategories();
        assertNotNull(categories);
        context.checking(new Expectations() {{
            one(dao).getEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(objects));
            one(dao).getActiveEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(objects));
            one(dao).getArchivedEntitiesForUser(ObservationCategory.class, user);
            will(returnValue(objects));
        }});
        assertEquals(output, categories.getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) categories.getActive()).getEntitiesForUser().getEntity());
        assertEquals(output, ((AppUserResource) categories.getArchived()).getEntitiesForUser().getEntity());
    }

    @Test
    public void testGettingMultipleEntityTypeDepthsNulls() throws Exception {
        // Not testing every permutation
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) ((AppUserResource) resource.getClassLists()).getObservations()).getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) ((AppUserResource) resource.getObservations()).getStudents()).getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) ((AppUserResource) resource.getPhotos()).getStudents()).getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) ((AppUserResource) resource.getStudents()).getClassLists()).getStatus());
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), ((Response) ((AppUserResource) resource.getObservationCategories()).getClassLists()).getStatus());
    }

    @Test
    public void testGetEntityResourceAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUserResource.class, "getAppUserEntityResource", "{entityId}");
    }

    @Test
    public void testGetAppUserEntityResource() throws Exception {
        final String someEntityId = "entity";
        AppUserResource resource = new AppUserResource(dao, serializer, userId, null, null);
        AppUserEntityResource entityResource = (AppUserEntityResource) resource.getAppUserEntityResource(someEntityId);
        assertNotNull(entityResource);
    }
}
