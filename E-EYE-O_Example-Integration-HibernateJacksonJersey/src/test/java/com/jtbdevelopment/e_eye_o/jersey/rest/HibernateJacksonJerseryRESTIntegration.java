package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserCreationHelper;
import com.jtbdevelopment.e_eye_o.entities.*;
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectEntitySettings;
import com.jtbdevelopment.e_eye_o.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectDeserializer;
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
 * <p/>
 * TODO - turn this into abstract test
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = {"integration"})
public class HibernateJacksonJerseryRESTIntegration extends AbstractTestNGSpringContextTests {

    public static final String APP_USER_OWNED_OBJECT = "appUserOwnedObject";
    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private JSONIdObjectSerializer jsonIdObjectSerializer;

    @Autowired
    private JSONIdObjectDeserializer jsonIdObjectDeserializer;

    @Autowired
    private ReadWriteDAO readWriteDAO;

    @Autowired
    private UserCreationHelper userCreationHelper;

    @Autowired
    private IdObjectFactory idObjectFactory;

    @Autowired
    private PersistentTokenBasedRememberMeServices rememberMeServices;

    @Autowired
    private IdObjectReflectionHelper idObjectReflectionHelper;

    private static AppUser testUser1 = null, testUser2 = null, testAdmin = null;

    private static HttpServer httpServer;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(9998).build();
    public static final String LOGIN_URI = BASE_URI + "v2/security/login/";
    public static final String USERS_URI = BASE_URI + "v2/users/";
    private HttpClient adminClient, userClient1, userClient2;

    @BeforeMethod
    public synchronized void setup() throws Exception {
        if (readWriteDAO != null && idObjectFactory != null && userCreationHelper != null) {

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
                userCreationHelper.createNewUser(testAdmin);
                userCreationHelper.createNewUser(testUser1);
                userCreationHelper.createNewUser(testUser2);
                testAdmin = readWriteDAO.getUser(testAdmin.getEmailAddress());
                testUser1 = readWriteDAO.getUser(testUser1.getEmailAddress());
                testUser2 = readWriteDAO.getUser(testUser2.getEmailAddress());

                readWriteDAO.create(idObjectFactory.newObservationCategoryBuilder(testUser1)
                        .withShortName("ARCHIVE")
                        .withDescription("Archived")
                        .withArchived(true).build());
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

        HttpResponse response = httpHelper.httpPostForm(uri, httpClient, formValues);
        logger.info(EntityUtils.toString(response.getEntity()));
        return httpClient;
    }

    @Test
    public void testGetVersion() throws Exception {
        String uri = BASE_URI + "/v2/security/version";
        HttpResponse response = httpHelper.httpGet(uri, adminClient);
        assertEquals("2.0", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testGetUserStandard() throws Exception {
        String uri = USERS_URI;
        httpHelper.checkJSONVsExpectedResults(uri, userClient1, Arrays.asList(testUser1));
    }

    @Test
    public void testGetUsersAdmin() throws Exception {
        String uri = USERS_URI;
        List<AppUser> expectedResults = Arrays.asList(testAdmin, testUser1, testUser2);
        httpHelper.checkJSONVsExpectedResults(uri, adminClient, expectedResults);
    }

    @Test
    public void testModifyUserAsSelf() throws Exception {
        AppUser testUser1 = httpHelper.easyClone(HibernateJacksonJerseryRESTIntegration.testUser1);
        testUser1.setLastName("New Last");
        testUser1.setAdmin(true);  // should be ignored
        testUser1.setActive(false);  // should be ignored
        testUser1.setActivated(false);  // should be ignored
        testUser1.setPassword("new"); //  should be ignored
        testUser1.setLastLogout(new DateTime());  // should be ignored

        String s = httpHelper.getJSONFromPut(USERS_URI + testUser1.getId() + "/", userClient1, testUser1);

        AppUser dbTestUser1 = readWriteDAO.get(AppUser.class, testUser1.getId());
        assertEquals(testUser1.getLastName(), dbTestUser1.getLastName());
        assertFalse(testUser1.getPassword().equals(dbTestUser1.getPassword()));
        assertFalse(dbTestUser1.isAdmin());
        assertTrue(dbTestUser1.isActivated());
        assertTrue(dbTestUser1.isActive());
        assertEquals(AppUser.NEVER_LOGGED_IN, dbTestUser1.getLastLogout());

        assertEquals(jsonIdObjectSerializer.write(dbTestUser1), s);

        HibernateJacksonJerseryRESTIntegration.testUser1 = dbTestUser1;
    }

    @Test
    public void testModifyUserAsAdmin() throws Exception {
        AppUser testUser2 = httpHelper.easyClone(HibernateJacksonJerseryRESTIntegration.testUser2);
        testUser2.setFirstName("newfirst");
        testUser2.setAdmin(true);
        testUser2.setActive(true);     //  should be ignored
        testUser2.setActivated(true);  //  should be ignored
        testUser2.setPassword("new");  //  should be ignored
        testUser2.setLastLogout(new DateTime());  // should be ignored

        String s = httpHelper.getJSONFromPut(USERS_URI + testUser2.getId() + "/", adminClient, testUser2);

        AppUser dbTestUser2 = readWriteDAO.get(AppUser.class, testUser2.getId());
        assertEquals(testUser2.getFirstName(), dbTestUser2.getFirstName());
        assertFalse(testUser2.getPassword().equals(dbTestUser2.getPassword()));
        assertTrue(dbTestUser2.isAdmin());
        assertFalse(dbTestUser2.isActivated());
        assertFalse(dbTestUser2.isActive());
        assertEquals(AppUser.NEVER_LOGGED_IN, dbTestUser2.getLastLogout());

        assertEquals(jsonIdObjectSerializer.write(dbTestUser2), s);
        HibernateJacksonJerseryRESTIntegration.testUser2 = dbTestUser2;
    }

    @Test
    public void testModifyingAnotherUserAsNonAdmin() throws Exception {
        AppUser user2 = httpHelper.easyClone(testUser2);
        user2.setLastName("Won't change");

        HttpResponse response = httpHelper.httpPut(USERS_URI + testUser2.getId() + "/", userClient1, user2);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(405, response.getStatusLine().getStatusCode());

        AppUser dbTestUser2 = readWriteDAO.get(AppUser.class, testUser2.getId());
        assertFalse(dbTestUser2.getLastName().equals(user2.getLastName()));
    }

    @Test
    public void testTryingToAddANewUserFails() throws Exception {
        AppUser newUser = idObjectFactory.newAppUserBuilder().withFirstName("New").withEmailAddress("newuser@new.com").build();

        HttpResponse response = httpHelper.httpPut(USERS_URI, userClient1, newUser);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(405, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testTryingToAddANewWithBadIdUserFails() throws Exception {
        AppUser newUser = idObjectFactory.newAppUserBuilder().withFirstName("New").withEmailAddress("newuser@new.com").withId("someid").build();

        HttpResponse response = httpHelper.httpPut(USERS_URI, userClient1, newUser);
        EntityUtils.consumeQuietly(response.getEntity());
        assertEquals(405, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testGetOwnObjects() throws Exception {
        Collection<AppUserOwnedObject> owned = filterUnreadableItems(readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, testUser1, 0, 0));
        String uri = USERS_URI + testUser1.getId() + "/";
        httpHelper.checkPaginatedJSONVsExpectedResults(uri, userClient1, owned);
    }

    @Test
    public void testGetOwnActiveObjects() throws Exception {
        Collection<AppUserOwnedObject> owned = filterUnreadableItems(readWriteDAO.getActiveEntitiesForUser(AppUserOwnedObject.class, testUser1, 0, 0));
        String uri = USERS_URI + testUser1.getId() + "/active/";
        httpHelper.checkPaginatedJSONVsExpectedResults(uri, userClient1, owned);
    }

    @Test
    public void testGetOwnArchivedObjects() throws Exception {
        Set<AppUserOwnedObject> owned = readWriteDAO.getArchivedEntitiesForUser(AppUserOwnedObject.class, testUser1, 0, 0);
        String uri = USERS_URI + testUser1.getId() + "/archived/";
        httpHelper.checkPaginatedJSONVsExpectedResults(uri, userClient1, owned);
    }

    @Test
    public void testGetOwnSubsetObjects() throws Exception {
        final String user_uri = USERS_URI + testUser1.getId() + "/";
        final String user_active_uri = USERS_URI + testUser1.getId() + "/active/";
        final String user_archived_uri = USERS_URI + testUser1.getId() + "/archived/";
        for (Class<? extends AppUserOwnedObject> entityClass : Arrays.asList(ClassList.class, Student.class, ObservationCategory.class, Observation.class, Photo.class)) {
            final String entityType = entityClass.getAnnotation(IdObjectEntitySettings.class).plural().toLowerCase() + "/";
            String uri = user_uri + entityType;
            httpHelper.checkPaginatedJSONVsExpectedResults(uri, userClient1, readWriteDAO.getEntitiesForUser(entityClass, testUser1, 0, 0));
            final Set<? extends AppUserOwnedObject> activeEntitiesForUser = readWriteDAO.getActiveEntitiesForUser(entityClass, testUser1, 0, 0);
            final Set<? extends AppUserOwnedObject> archivedEntitiesForUser = readWriteDAO.getArchivedEntitiesForUser(entityClass, testUser1, 0, 0);
            httpHelper.checkPaginatedJSONVsExpectedResults(uri + "active/", userClient1, activeEntitiesForUser);
            httpHelper.checkPaginatedJSONVsExpectedResults(uri + "archived/", userClient1, archivedEntitiesForUser);
            httpHelper.checkPaginatedJSONVsExpectedResults(user_active_uri + entityType, userClient1, activeEntitiesForUser);
            httpHelper.checkPaginatedJSONVsExpectedResults(user_archived_uri + entityType, userClient1, archivedEntitiesForUser);
        }
    }

    @Test
    public void testGetOwnObject() throws Exception {
        List<AppUserOwnedObject> owned = new ArrayList<>(readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, testUser1, 0, 0));
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
        Collection<AppUserOwnedObject> owned = filterUnreadableItems(readWriteDAO.getEntitiesForUser(AppUserOwnedObject.class, testUser1, 0, 0));
        String uri = USERS_URI + testUser1.getId() + "/";
        httpHelper.checkPaginatedJSONVsExpectedResults(uri, adminClient, owned);
    }

    private <T extends AppUserOwnedObject> Collection<T> filterUnreadableItems(final Set<T> results) {
        return Collections2.filter(results, new Predicate<T>() {
            @Override
            public boolean apply(@Nullable T input) {
                if (input == null) {
                    return false;
                }
                Class<? extends AppUserOwnedObject> idObjectInterface = idObjectReflectionHelper.getIdObjectInterfaceForClass(input.getClass());
                return idObjectInterface.getAnnotation(IdObjectEntitySettings.class).viewable();
            }
        });
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
        ObservationCategory readCategory = jsonIdObjectDeserializer.readAsObjects(newJson);
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
        ClassList createdClassList = jsonIdObjectDeserializer.readAsObjects(newJson);

        String newDescription = "Modified Class";
        createdClassList.setDescription(newDescription);
        newJson = httpHelper.getJSONFromPut(uri, userClient1, createdClassList);
        ClassList modifiedClassList = jsonIdObjectDeserializer.readAsObjects(newJson);
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
        final Student createdStudent = jsonIdObjectDeserializer.readAsObjects(newJson);

        DateTime now = DateTime.now();
        Thread.sleep(1000);

        response = httpHelper.httpDelete(uri, userClient1);
        assertEquals(javax.ws.rs.core.Response.Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());
        EntityUtils.consumeQuietly(response.getEntity());

        assertNull(readWriteDAO.get(Student.class, createdStudent.getId()));
        @SuppressWarnings("unchecked")
        Collection<DeletedObject> deletedObjects = (Collection<DeletedObject>) Collections2.filter(readWriteDAO.getModificationsSince(testUser1, now, "", 0), new Predicate<AppUserOwnedObject>() {
                    @Override
                    public boolean apply(@Nullable final AppUserOwnedObject input) {
                        return input != null && input instanceof DeletedObject;
                    }
                }
        );
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
