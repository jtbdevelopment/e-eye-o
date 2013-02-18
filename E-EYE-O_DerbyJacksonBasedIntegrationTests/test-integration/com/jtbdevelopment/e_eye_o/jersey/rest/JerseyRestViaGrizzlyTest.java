package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Date: 2/10/13
 * Time: 4:05 PM
 */
@ContextConfiguration("/test-integration-context.xml")
@Test(groups = {"integration"})
public class JerseyRestViaGrizzlyTest extends AbstractTestNGSpringContextTests {
    private static HttpServer httpServer;
    private static final URI BASE_URI = UriBuilder.fromUri("http://localhost/").port(9998).build();

    @BeforeMethod
    public synchronized void setup() {
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
    }

    @AfterGroups({"integration"})
    public synchronized void teardown() {
        if (httpServer == null) {
            return;
        }
/*
        System.out.println("Press enter to stop");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        httpServer.stop();
    }

    @Test
    public void doNothingTest() {

    }
}
