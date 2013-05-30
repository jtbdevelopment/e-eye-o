package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.PreferredDescription;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.servlet.FilterRegistration;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.testng.AssertJUnit.*;

/**
 * Date: 2/10/13
 * Time: 4:05 PM
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = {"integration"})
public class JerseyRestViaGrizzlyIntegration extends AbstractTestNGSpringContextTests {

    public static final String APP_USER = "appUser";
    public static final String APP_USER_OWNED_OBJECT = "appUserOwnedObject";
    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    private static AppUser testUser1 = null, testUser2 = null, testAdmin = null;

    private static HttpServer httpServer;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(9998).build();
    public static final String LOGIN_URI = BASE_URI + "security/login/";
    public static final String USERS_URI = BASE_URI + "users/";
    private HttpClient adminClient, userClient1, userClient2;

    @BeforeMethod
    public synchronized void setup() throws Exception {
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
                testAdmin = readWriteDAO.getUser(testAdmin.getEmailAddress());
                testUser1 = readWriteDAO.getUser(testUser1.getEmailAddress());
                testUser2 = readWriteDAO.getUser(testUser2.getEmailAddress());

                readWriteDAO.create(idObjectFactory.newObservationCategoryBuilder(testUser1)
                        .withShortName("ARCHIVE")
                        .withDescription("Archived")
                        .withArchiveFlag(true).build());
            }
        }

        if (httpServer != null) {
            return;
        }

        try {
            final Map<String, String> initParams = new HashMap<>();
            initParams.put(ClassNamesResourceConfig.PROPERTY_CLASSNAMES, "com.jtbdevelopment.e_eye_o");

            httpServer = GrizzlyServerFactory.createHttpServer(BASE_URI, new HttpHandler() {
                @Override
                public void service(Request request, Response response) throws Exception {
                }
            });

            WebappContext webappContext = new WebappContext("context", "/");
            ServletRegistration registration = webappContext.addServlet("spring", SpringServlet.class);
            registration.setInitParameters(initParams);
            registration.addMapping("/*");
            webappContext.addContextInitParameter("contextConfigLocation", "classpath:test-integration-server-context.xml");
            webappContext.addListener("org.springframework.web.context.ContextLoaderListener");
            webappContext.addListener("org.springframework.web.context.request.RequestContextListener");
            FilterRegistration filterRegistration = webappContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
            filterRegistration.addMappingForUrlPatterns(null, "/*");
            webappContext.deploy(httpServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        adminClient = createHttpClientSession(testAdmin);
        userClient1 = createHttpClientSession(testUser1);
        userClient2 = createHttpClientSession(testUser2);
    }

    @AfterGroups({"integration"})
    public synchronized void teardown() {
        if (httpServer == null) {
            return;
        }
        httpServer.stop();
    }

    public HttpClient createHttpClientSession(final AppUser user) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        String uri = LOGIN_URI + "?" + rememberMeServices.getParameter() + "=true";
        List<NameValuePair> formValues = new LinkedList<>();
        formValues.add(new BasicNameValuePair("login", user.getEmailAddress()));
        formValues.add(new BasicNameValuePair("password", user.getPassword()));
        formValues.add(new BasicNameValuePair(rememberMeServices.getParameter(), "true"));

        HttpResponse response = httpHelper.httpPost(uri, httpClient, formValues);
        logger.info(EntityUtils.toString(response.getEntity()));
        return httpClient;
    }

    @Test
    public void testGetVersion() throws Exception {
        String uri = BASE_URI + "/security/version";
        HttpResponse response = httpHelper.httpGet(uri, adminClient);
        assertEquals("1.0", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testGetUserStandard() throws Exception {
        String uri = USERS_URI;
        httpHelper.checkJSONVsExpectedResult(uri, userClient1, testUser1);
    }

    @Test
    public void testGetUsersAdmin() throws Exception {
        String uri = BASE_URI + "users/";
        List<AppUser> expectedResults = Arrays.asList(testAdmin, testUser1, testUser2);
        httpHelper.checkJSONVsExpectedResults(uri, adminClient, expectedResults);
    }

    @Test
    public void testModifyUserAsSelf() throws Exception {
        AppUser testUser1 = httpHelper.easyClone(JerseyRestViaGrizzlyIntegration.testUser1);
        testUser1.setLastName("New Last");
        testUser1.setAdmin(true);  // should be ignored
        testUser1.setActive(false);  // should be ignored
        testUser1.setActivated(false);  // should be ignored
        testUser1.setPassword("new"); //  should be ignored
        testUser1.setLastLogout(new DateTime());  // should be ignored

        String s = httpHelper.getJSONFromPut(USERS_URI, userClient1, APP_USER, testUser1);

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
    public void testModifyUserAsAdmin() throws Exception {
        AppUser testUser2 = httpHelper.easyClone(JerseyRestViaGrizzlyIntegration.testUser2);
        testUser2.setFirstName("newfirst");
        testUser2.setAdmin(true);
        testUser2.setActive(true);
        testUser2.setActivated(true);
        testUser2.setPassword("new");
        testUser2.setLastLogout(new DateTime());  // should be ignored

        String s = httpHelper.getJSONFromPut(USERS_URI, adminClient, APP_USER, testUser2);

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

    @Test
    public void testModifyingAnotherUserAsNonAdmin() throws Exception {
        AppUser user2 = httpHelper.easyClone(testUser2);
        user2.setLastName("Won't change");

        HttpResponse response = httpHelper.httpPut(USERS_URI, userClient1, APP_USER, user2);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(javax.ws.rs.core.Response.Status.FORBIDDEN.getStatusCode(), response.getStatusLine().getStatusCode());

        AppUser dbTestUser2 = readWriteDAO.get(AppUser.class, testUser2.getId());
        assertFalse(dbTestUser2.getLastName().equals(user2.getLastName()));
    }

    @Test
    public void testTryingToAddANewUserFails() throws Exception {
        AppUser newUser = idObjectFactory.newAppUserBuilder().withFirstName("New").withEmailAddress("newuser@new.com").build();

        HttpResponse response = httpHelper.httpPut(USERS_URI, userClient1, APP_USER, newUser);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    }

    @Test
    public void testTryingToAddANewWithBadIdUserFails() throws Exception {
        AppUser newUser = idObjectFactory.newAppUserBuilder().withFirstName("New").withEmailAddress("newuser@new.com").withId("someid").build();

        HttpResponse response = httpHelper.httpPut(USERS_URI, userClient1, APP_USER, newUser);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(javax.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetOwnObjects() throws Exception {
        Set<AppUserOwnedObject> owned = readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, testUser1);
        String uri = USERS_URI + testUser1.getId() + "/";
        httpHelper.checkJSONVsExpectedResults(uri, userClient1, owned);
    }

    @Test
    public void testGetOwnActiveObjects() throws Exception {
        Set<AppUserOwnedObject> owned = readWriteDAO.getActiveEntitiesForUser(AppUserOwnedObject.class, testUser1);
        String uri = USERS_URI + testUser1.getId() + "/active/";
        httpHelper.checkJSONVsExpectedResults(uri, userClient1, owned);
    }

    @Test
    public void testGetOwnArchivedObjects() throws Exception {
        Set<AppUserOwnedObject> owned = readWriteDAO.getArchivedEntitiesForUser(AppUserOwnedObject.class, testUser1);
        String uri = USERS_URI + testUser1.getId() + "/archived/";
        httpHelper.checkJSONVsExpectedResults(uri, userClient1, owned);
    }

    @Test
    public void testGetOwnSubsetObjects() throws Exception {
        final String user_uri = USERS_URI + testUser1.getId() + "/";
        final String user_active_uri = USERS_URI + testUser1.getId() + "/active/";
        final String user_archived_uri = USERS_URI + testUser1.getId() + "/archived/";
        for (Class<? extends AppUserOwnedObject> entityClass : Arrays.asList(ClassList.class, Student.class, ObservationCategory.class, Observation.class, Photo.class)) {
            final String entityType = entityClass.getAnnotation(PreferredDescription.class).plural().toLowerCase() + "/";
            String uri = user_uri + entityType;
            httpHelper.checkJSONVsExpectedResults(uri, userClient1, readWriteDAO.getEntitiesForUser(entityClass, testUser1));
            final Set<? extends AppUserOwnedObject> activeEntitiesForUser = readWriteDAO.getActiveEntitiesForUser(entityClass, testUser1);
            final Set<? extends AppUserOwnedObject> archivedEntitiesForUser = readWriteDAO.getArchivedEntitiesForUser(entityClass, testUser1);
            httpHelper.checkJSONVsExpectedResults(uri + "active/", userClient1, activeEntitiesForUser);
            httpHelper.checkJSONVsExpectedResults(uri + "archived/", userClient1, archivedEntitiesForUser);
            httpHelper.checkJSONVsExpectedResults(user_active_uri + entityType, userClient1, activeEntitiesForUser);
            httpHelper.checkJSONVsExpectedResults(user_archived_uri + entityType, userClient1, archivedEntitiesForUser);
        }
    }

    @Test
    public void testGetOwnObject() throws Exception {
        List<AppUserOwnedObject> owned = new ArrayList<>(readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, testUser1));
        AppUserOwnedObject randomOne = owned.get(new Random().nextInt(owned.size()));
        String uri = USERS_URI + testUser1.getId() + "/" + randomOne.getId() + "/";
        httpHelper.checkJSONVsExpectedResult(uri, userClient1, randomOne);
    }

    @Test
    public void testGetAnotherUsersObjectsAsNonAdmin() throws Exception {
        String uri = USERS_URI + testUser2.getId() + "/";
        HttpResponse response = httpHelper.httpGet(uri, userClient1);

        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(405, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetAnotherUsersObjectsAsAdmin() throws Exception {
        Set<AppUserOwnedObject> owned = readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, testUser1);
        String uri = USERS_URI + testUser1.getId() + "/";
        httpHelper.checkJSONVsExpectedResults(uri, adminClient, owned);
    }

    @Test
    public void testCreatingNewObjectAsSelf() throws Exception {
        String uri = USERS_URI + testUser1.getId() + "/";
        String description = "By Rest";
        String rest = "REST";
        DateTime now = new DateTime();
        ObservationCategory category = idObjectFactory.newObservationCategoryBuilder(testUser1).withDescription(description).withShortName(rest).build();
        HttpResponse response = httpHelper.httpPost(uri, userClient1, APP_USER_OWNED_OBJECT, category);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(javax.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.getStatusLine().getStatusCode());
        Header[] headers = response.getHeaders(HttpHeaders.LOCATION);
        assertEquals(1, headers.length);

        uri = headers[0].getValue();
        String newJson = httpHelper.getJSONFromHttpGet(uri, userClient1);
        ObservationCategory readCategory = jsonIdObjectSerializer.read(newJson);
        assertEquals(category.getDescription(), readCategory.getDescription());
        assertEquals(category.getShortName(), readCategory.getShortName());
        assertTrue(readCategory.getModificationTimestamp().isAfter(now));
    }

    @Test
    public void testModifyingObjectAsSelf() throws Exception {
        String uri = USERS_URI + testUser1.getId() + "/";
        ClassList classList = idObjectFactory.newClassListBuilder(testUser1).withDescription("Modify Class").build();
        HttpResponse response = httpHelper.httpPost(uri, userClient1, APP_USER_OWNED_OBJECT, classList);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(javax.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.getStatusLine().getStatusCode());
        Header[] headers = response.getHeaders(HttpHeaders.LOCATION);
        assertEquals(1, headers.length);
        uri = headers[0].getValue();
        String newJson = httpHelper.getJSONFromHttpGet(uri, userClient1);
        ClassList createdClassList = jsonIdObjectSerializer.read(newJson);

        String newDescription = "Modified Class";
        createdClassList.setDescription(newDescription);
        newJson = httpHelper.getJSONFromPut(uri, userClient1, APP_USER_OWNED_OBJECT, createdClassList);
        ClassList modifiedClassList = jsonIdObjectSerializer.read(newJson);
        assertTrue(modifiedClassList.equals(createdClassList));
        assertEquals(newDescription, modifiedClassList.getDescription());
        assertTrue(modifiedClassList.getModificationTimestamp().isAfter(createdClassList.getModificationTimestamp()));
    }

    @Test
    public void testDeletingObjectAsSelf() throws Exception {
        String uri = USERS_URI + testUser1.getId() + "/";
        Student student = idObjectFactory.newStudentBuilder(testUser1).withFirstName("Delete").withLastName("Delete").build();
        HttpResponse response = httpHelper.httpPost(uri, userClient1, APP_USER_OWNED_OBJECT, student);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(javax.ws.rs.core.Response.Status.CREATED.getStatusCode(), response.getStatusLine().getStatusCode());
        Header[] headers = response.getHeaders(HttpHeaders.LOCATION);
        assertEquals(1, headers.length);
        uri = headers[0].getValue();
        String newJson = httpHelper.getJSONFromHttpGet(uri, userClient1);
        final Student createdStudent = jsonIdObjectSerializer.read(newJson);

        DateTime now = DateTime.now();

        response = httpHelper.httpDelete(uri, userClient1);
        assertEquals(javax.ws.rs.core.Response.Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());

        assertNull(readWriteDAO.get(Student.class, createdStudent.getId()));
        Collection<DeletedObject> deletedObjects = readWriteDAO.getEntitiesForUser(DeletedObject.class, testUser1);
        deletedObjects = Collections2.filter(deletedObjects, new Predicate<DeletedObject>() {
            @Override
            public boolean apply(@Nullable DeletedObject input) {
                return input != null && input.getDeletedId().equals(createdStudent.getId());
            }
        });
        assertEquals(1, deletedObjects.size());
        assertTrue(deletedObjects.iterator().next().getModificationTimestamp().isAfter(now));
    }
}
