package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.DAO.helpers.UserHelper;
import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObjectFactory;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.ClassNamesResourceConfig;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

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
    private static final String REST_BASE = "http://localhost:9998/REST/";
    private WebResource baseAdmin;
    private WebResource usersAdmin;
    private WebResource securityAdmin;
    private WebResource loginAdmin;
    private WebResource logoutAdmin;
    private WebResource baseUser1;
    private WebResource usersUser1;
    private WebResource securityUser1;
    private WebResource loginUser1;
    private WebResource logoutUser1;
    private WebResource baseUser2;
    private WebResource usersUser2;
    private WebResource securityUser2;
    private WebResource loginUser2;
    private WebResource logoutUser2;
    private HttpClient adminClient;

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
            //initParams.put(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, "")

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
//            filterRegistration.addMappingForServletNames(null, "spring);
            filterRegistration.addMappingForUrlPatterns(null, "/*");
            webappContext.deploy(httpServer);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        java.net.CookieManager cm = new java.net.CookieManager();
//        java.net.CookieHandler.setDefault(cm);

        List<NameValuePair> login = new LinkedList<>();
        UrlEncodedFormEntity loginForm;
        HttpPost loginPost;

        adminClient = new DefaultHttpClient();
        login.clear();
        login.add(new BasicNameValuePair("login", testAdmin.getEmailAddress()));
        login.add(new BasicNameValuePair("password", testAdmin.getPassword()));
        login.add(new BasicNameValuePair(rememberMeServices.getParameter(), "true"));
        loginForm = new UrlEncodedFormEntity(login);
        loginPost = new HttpPost(BASE_URI + "security/login/?" + rememberMeServices.getParameter() + "=true");
        loginPost.setEntity(loginForm);
        HttpResponse response = adminClient.execute(loginPost);
        logger.info(EntityUtils.toString(response.getEntity()));
        /*
        Client adminClient = Client.create();
        baseAdmin = adminClient.resource(REST_BASE);
        usersAdmin = baseAdmin.path("users/");
        securityAdmin = baseAdmin.path("security/");
        loginAdmin = securityAdmin.path("login/");
        logoutAdmin = securityAdmin.path("logout/");
        Form form = new Form();
        form.add("login", testAdmin.getEmailAddress());
        form.add("password", testAdmin.getPassword());
        loginAdmin.accept(MediaType.TEXT_PLAIN).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class, form);
        */
        /*
        Client user1Client = Client.create();
        baseUser1 = user1Client.resource(BASE_URI);
        usersUser1 = baseUser1.path("users/");
        securityUser1 = baseUser1.path("security/");
        loginUser1 = securityUser1.path("login/");
        logoutUser1 = securityUser1.path("logout/");
        form = new Form();
        form.add("login", testUser1.getEmailAddress());
        form.add("password", testUser1.getPassword());
        loginUser1.accept(MediaType.TEXT_PLAIN).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class, form);

        Client user2Client = Client.create();
        baseUser2 = user2Client.resource(BASE_URI);
        usersUser2 = baseUser2.path("users/");
        securityUser2 = baseUser2.path("security/");
        loginUser2 = securityUser2.path("login/");
        logoutUser2 = securityUser2.path("logout/");
        form = new Form();
        form.add("login", testUser2.getEmailAddress());
        form.add("password", testUser2.getPassword());
        loginUser2.accept(MediaType.TEXT_PLAIN).type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class, form);
        */
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

    /*
    @Test
    public void testGetUserStandard() throws Exception {
        String s = usersUser1.accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);
        assertEquals(jsonIdObjectSerializer.write(testUser1), s);
        assertEquals("", ((AppUser) jsonIdObjectSerializer.read(s)).getPassword());
    }

    @Test
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
        String s = usersUser1.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED).put(String.class, f);

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
    */

    @Test
    public void testGetUsersAdmin() throws Exception {
        HttpGet get = new HttpGet(BASE_URI + "users/");

        HttpResponse response = adminClient.execute(get);
        assertEquals(MediaType.APPLICATION_JSON, response.getEntity().getContentType().getValue());

        String json = EntityUtils.toString(response.getEntity());
        logger.info(json);
        List<AppUser> users = jsonIdObjectSerializer.read(json);

        assertTrue(users.containsAll(Arrays.asList(testAdmin, testUser1, testUser2)));
    }

    /*
    @Test
    public void testModifyUserAsAdmin() throws Exception {
        AppUser testUser2 = jsonIdObjectSerializer.read(jsonIdObjectSerializer.write(JerseyRestViaGrizzlyIntegration.testUser2));
        testUser2.setFirstName("newfirst");
        testUser2.setAdmin(true);
        testUser2.setActive(true);
        testUser2.setActivated(true);
        testUser2.setPassword("new");
        testUser2.setLastLogout(new DateTime());  // should be ignored

        Form f = new Form();
        f.add("appUser", jsonIdObjectSerializer.write(testUser2));
        String s = usersAdmin.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_FORM_URLENCODED).put(String.class, f);

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
*/

}
