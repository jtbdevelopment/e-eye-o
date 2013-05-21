package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

import javax.ws.rs.core.MediaType;
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

    public HttpClient createHttpClientSession(final AppUser user) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        String uri = LOGIN_URI + "?" + rememberMeServices.getParameter() + "=true";
        List<NameValuePair> formValues = new LinkedList<>();
        formValues.add(new BasicNameValuePair("login", user.getEmailAddress()));
        formValues.add(new BasicNameValuePair("password", user.getPassword()));
        formValues.add(new BasicNameValuePair(rememberMeServices.getParameter(), "true"));

        HttpResponse response = httpPost(httpClient, uri, formValues);
        logger.info(EntityUtils.toString(response.getEntity()));
        return httpClient;
    }

    private HttpResponse httpPost(HttpClient httpClient, String uri, List<NameValuePair> formValues) throws IOException {
        UrlEncodedFormEntity postForm = new UrlEncodedFormEntity(formValues);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(postForm);
        return httpClient.execute(httpPost);
    }

    private HttpResponse httpPut(HttpClient httpClient, String uri, List<NameValuePair> formValues) throws IOException {
        UrlEncodedFormEntity postForm = new UrlEncodedFormEntity(formValues);
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setEntity(postForm);
        return httpClient.execute(httpPut);
    }

    @AfterGroups({"integration"})
    public synchronized void teardown() {
        if (httpServer == null) {
            return;
        }
        httpServer.stop();
    }

    @Test
    public void testGetUserStandard() throws Exception {
        String uri = USERS_URI;
        checkJSONValue(uri, testUser1, userClient1);
    }

    @Test
    public void testModifyUserAsSelf() throws Exception {
        AppUser testUser1 = easyClone(JerseyRestViaGrizzlyIntegration.testUser1);
        testUser1.setLastName("New Last");
        testUser1.setAdmin(true);  // should be ignored
        testUser1.setActive(false);  // should be ignored
        testUser1.setActivated(false);  // should be ignored
        testUser1.setPassword("new"); //  should be ignored
        testUser1.setLastLogout(new DateTime());  // should be ignored

        String s = getJSONFromPut(testUser1, "appUser", userClient1, USERS_URI);

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

    private <T extends IdObject> String getJSONFromPut(final T entity, String param, HttpClient client, String uri) throws IOException {
        List<NameValuePair> formValues = new LinkedList<>();
        formValues.add(new BasicNameValuePair(param, jsonIdObjectSerializer.write(entity)));
        HttpResponse response = httpPut(client, uri, formValues);
        return getJSONFromResponse(response);
    }

    private <T extends IdObject> T easyClone(final T entity) {
        return jsonIdObjectSerializer.read(jsonIdObjectSerializer.write(entity));
    }

    @Test
    public void testGetUsersAdmin() throws Exception {
        String uri = BASE_URI + "users/";
        List<AppUser> expectedResults = Arrays.asList(testAdmin, testUser1, testUser2);
        checkJSONValues(uri, expectedResults, adminClient);
    }

    private <T extends IdObject> void checkJSONValue(String uri, T expectedResult, HttpClient client) throws IOException {
        String json = getJSONFromHttpGet(uri, client);
        assertEquals(json, jsonIdObjectSerializer.write(expectedResult));
    }

    private <T extends IdObject> void checkJSONValues(String uri, List<T> expectedResults, HttpClient client) throws IOException {
        String json = getJSONFromHttpGet(uri, client);
        List<T> results = jsonIdObjectSerializer.read(json);
        assertTrue(results.containsAll(expectedResults));
    }

    private String getJSONFromHttpGet(String uri, HttpClient client) throws IOException {
        HttpResponse response = httpGet(uri, client);
        return getJSONFromResponse(response);
    }

    private String getJSONFromResponse(HttpResponse response) throws IOException {
        assertEquals(MediaType.APPLICATION_JSON, response.getEntity().getContentType().getValue());
        String json = EntityUtils.toString(response.getEntity());
        logger.info(json);
        return json;
    }

    private HttpResponse httpGet(String uri, HttpClient client) throws IOException {
        HttpGet get = new HttpGet(uri);
        return client.execute(get);
    }

    @Test
    public void testModifyUserAsAdmin() throws Exception {
        AppUser testUser2 = easyClone(JerseyRestViaGrizzlyIntegration.testUser2);
        testUser2.setFirstName("newfirst");
        testUser2.setAdmin(true);
        testUser2.setActive(true);
        testUser2.setActivated(true);
        testUser2.setPassword("new");
        testUser2.setLastLogout(new DateTime());  // should be ignored

        String s = getJSONFromPut(testUser2, "appUser", adminClient, USERS_URI);

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
