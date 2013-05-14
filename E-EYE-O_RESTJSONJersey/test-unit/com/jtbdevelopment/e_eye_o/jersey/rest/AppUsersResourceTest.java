package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Date: 2/18/13
 * Time: 3:46 PM
 */
public class AppUsersResourceTest extends AbstractResourceTest {
    protected AppUsersResource resource;

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        resource = new AppUsersResource();
        resource.readWriteDAO = dao;
        resource.jsonIdObjectSerializer = serializer;
    }

    @Test
    public void testGetUsersAnnotations() throws NoSuchMethodException {
        checkGETJSONForMethod(AppUsersResource.class, "getUsers");
    }

    @Test
    public void testGetUsers() throws Exception {
        final Set<AppUser> daoResults = new HashSet<>();
        final String serialString = "{ \"field\": 0 }";
        context.checking(new Expectations() {{
            one(dao).getUsers();
            will(returnValue(daoResults));
            one(serializer).write(daoResults);
            will(returnValue(serialString));
        }});

        assertEquals(serialString, resource.getUsers());
    }

    @Test
    public void testGetUserEntitiesAnnotations() throws NoSuchMethodException {
        checkPathForMethod(AppUsersResource.class, "getUserEntities", "{userId}");
    }

    @Test
    public void testGetUserEntities() throws Exception {
        final String userId = "an=id";
        final AppUser appUser = context.mock(AppUser.class);
        context.checking(new Expectations() {{
            one(dao).get(AppUser.class, userId);
            will(returnValue(appUser));
        }});
        assertNotNull(resource.getUserEntities(userId));
    }


}
