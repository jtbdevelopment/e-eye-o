package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import org.jmock.Expectations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Date: 2/18/13
 * Time: 4:16 PM
 */
public class AppUserEntityResourceTest extends AbstractResourceTest {
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGetEntityAnnotations() throws NoSuchMethodException {
        checkGETJSONForMethod(AppUserEntityResource.class, "getEntity");
    }

    @Test
    public void testGetEntity() throws Exception {
        final String entityId = "xyz-123";
        final AppUserOwnedObject ownedObject = context.mock(AppUserOwnedObject.class);
        final String serialized = "somestring";
        context.checking(new Expectations(){{
            one(dao).get(AppUserOwnedObject.class, entityId);
            will(returnValue(ownedObject));
            one(serializer).write(ownedObject);
            will(returnValue(serialized));
        }});
        AppUserEntityResource resource = new AppUserEntityResource(dao, serializer, entityId);
        assertEquals(serialized, resource.getEntity());

    }
}
