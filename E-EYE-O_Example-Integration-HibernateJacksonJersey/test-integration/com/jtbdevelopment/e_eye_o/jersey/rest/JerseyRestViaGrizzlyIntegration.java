package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.*;

/**
 * Date: 2/10/13
 * Time: 4:05 PM
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = {"integration"})
public class JerseyRestViaGrizzlyIntegration extends AbstractTestNGSpringContextTests {
    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private IdObjectFactory idObjectFactory;

    private static AppUser testUser1 = null, testUser2 = null, testAdmin = null;

    private static HttpServer httpServer;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(9998).build();
    private Client c;
    private WebResource baseWebResource;
    private WebResource usersWebResource;

    @BeforeMethod
    public synchronized void setup() {
        if (readWriteDAO != null && idObjectFactory != null && userHelper != null) {

            String user1EmailAddress = "user1@rest.com";
            String user2EmailAddress = "user2@rest.com";
            String adminEmailAddress = "admin@rest.com";
            if (testAdmin == null) {
                testAdmin = readWriteDAO.getUser(adminEmailAddress);
                testUser1 = readWriteDAO.getUser(user1EmailAddress);
                testUser2 = readWriteDAO.getUser(user2EmailAddress);
            }

            if (testAdmin == null) {
                testAdmin = idObjectFactory.newAppUserBuilder()
                        .withActivated(true)
                        .withActive(true)
                        .withAdmin(true)
                        .withEmailAddress(adminEmailAddress)
                        .withFirstName("admin")
                        .withLastName("rest")
                        .withPassword("admin")
                        .build();
                testUser1 = idObjectFactory.newAppUserBuilder()
                        .withActivated(true)
                        .withActive(true)
                        .withAdmin(false)
                        .withEmailAddress(user1EmailAddress)
                        .withFirstName("user1")
                        .withLastName("rest")
                        .withPassword("user1")
                        .build();
                testUser2 = idObjectFactory.newAppUserBuilder()
                        .withActivated(false)
                        .withActive(false)
                        .withAdmin(false)
                        .withEmailAddress(user2EmailAddress)
                        .withFirstName("user2")
                        .withLastName("rest")
                        .withPassword("user2")
                        .build();
                userHelper.setUpNewUser(testAdmin);
                userHelper.setUpNewUser(testUser1);
                userHelper.setUpNewUser(testUser2);
            }
        }

        //  Inject user 1 by default
        SecurityAwareResource.setInjectUser(testUser1);

        if (httpServer != null) {
            return;
        }

        ResourceConfig rc = new PackagesResourceConfig("com.jtbdevelopment.e_eye_o");
        try {
            SpringComponentProviderFactory spring = new SpringComponentProviderFactory(rc, (ConfigurableApplicationContext) this.applicationContext);
            httpServer = GrizzlyServerFactory.createHttpServer(BASE_URI, rc, spring);
        } catch (IOException e) {
            e.printStackTrace();
        }

        c = Client.create();
        baseWebResource = c.resource(BASE_URI);
        usersWebResource = baseWebResource.path("users");
    }

    @AfterGroups({"integration"})
    public synchronized void teardown() {
        if (httpServer == null) {
            return;
        }
        httpServer.stop();
    }

    @Test
    public void doNothingTest() {

    }

    @Test
    public void testGetUserStandard() throws Exception {
        String s = usersWebResource.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
        assertEquals(jsonIdObjectSerializer.write(testUser1), s);
        assertEquals("", ((AppUser) jsonIdObjectSerializer.read(s)).getPassword());
    }

    public void testModifyUserAsSelf() throws Exception {
        AppUser testUser1 = jsonIdObjectSerializer.read(jsonIdObjectSerializer.write(JerseyRestViaGrizzlyIntegration.testUser1));
        testUser1.setLastName("New Last");
        testUser1.setAdmin(true);  // should be ignored
        testUser1.setActive(false);  // should be ignored
        testUser1.setActivated(false);  // should be ignored
        testUser1.setPassword("new"); //  should be ignored
        testUser1.setLastLogout(new DateTime());  // should be ignored

        Form f = new Form();
        f.add("appUser", jsonIdObjectSerializer.write(testUser1));
        String s = usersWebResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED).put(String.class, f);

        AppUser dbTestUser1 = readWriteDAO.get(AppUser.class, testUser1.getId());
        assertEquals(testUser1.getLastName(), dbTestUser1.getLastName());
        assertFalse(testUser1.getPassword().equals(dbTestUser1.getPassword()));
        assertFalse(dbTestUser1.isAdmin());
        assertTrue(dbTestUser1.isActivated());
        assertTrue(dbTestUser1.isActive());
        assertEquals(AppUser.NEVER_LOGGED_IN, dbTestUser1.getLastLogout());

        assertEquals(jsonIdObjectSerializer.write(dbTestUser1), s);
        JerseyRestViaGrizzlyIntegration.testUser1 = dbTestUser1;
    }

    @Test
    public void testGetUsersAdmin() throws Exception {
        SecurityAwareResource.setInjectUser(testAdmin);
        List<AppUser> users = jsonIdObjectSerializer.read(usersWebResource.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class));

        assertTrue(users.containsAll(Arrays.asList(testAdmin, testUser1, testUser2)));
    }

    public void testModifyUserAsAdmin() throws Exception {
        SecurityAwareResource.setInjectUser(testAdmin);
        AppUser testUser2 = jsonIdObjectSerializer.read(jsonIdObjectSerializer.write(this.testUser2));
        testUser2.setFirstName("newfirst");
        testUser2.setAdmin(true);
        testUser2.setActive(true);
        testUser2.setActivated(true);
        testUser2.setPassword("new");
        testUser2.setLastLogout(new DateTime());  // should be ignored

        Form f = new Form();
        f.add("appUser", jsonIdObjectSerializer.write(testUser2));
        String s = usersWebResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED).put(String.class, f);

        AppUser dbTestUser2 = readWriteDAO.get(AppUser.class, testUser2.getId());
        assertEquals(testUser2.getFirstName(), dbTestUser2.getFirstName());
        assertFalse(testUser2.getPassword().equals(dbTestUser2.getPassword()));
        assertTrue(dbTestUser2.isAdmin());
        assertTrue(dbTestUser2.isActivated());
        assertTrue(dbTestUser2.isActive());
        assertEquals(AppUser.NEVER_LOGGED_IN, dbTestUser2.getLastLogout());

        assertEquals(jsonIdObjectSerializer.write(dbTestUser2), s);
        JerseyRestViaGrizzlyIntegration.testUser2 = dbTestUser2;
    }


}
