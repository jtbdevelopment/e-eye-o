package com.jtbdevelopment.e_eye_o.jersey.rest;

import com.jtbdevelopment.e_eye_o.DAO.ReadWriteDAO;
import com.jtbdevelopment.e_eye_o.entities.reflection.IdObjectReflectionHelper;
import com.jtbdevelopment.e_eye_o.serialization.JSONIdObjectSerializer;
import org.jmock.Mockery;
import org.testng.annotations.BeforeMethod;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Date: 2/18/13
 * Time: 4:09 PM
 */
public class AbstractResourceTest {
    protected Mockery context;
    protected ReadWriteDAO dao;
    protected JSONIdObjectSerializer serializer;
    protected IdObjectReflectionHelper reflectionHelper;

    @BeforeMethod
    public void setUp() throws Exception {
        context = new Mockery();
        dao = context.mock(ReadWriteDAO.class);
        reflectionHelper = context.mock(IdObjectReflectionHelper.class);
        serializer = context.mock(JSONIdObjectSerializer.class);
    }

    protected void checkPathForMethod(final Class<?> resourceClass, final String method, final String pathValue) throws NoSuchMethodException {
        boolean foundPath = false;
        Method methodInfo;
        if (pathValue.startsWith("{")) {
            methodInfo = resourceClass.getMethod(method, String.class);
        } else {
            methodInfo = resourceClass.getMethod(method);
        }
        for (Annotation annotation : methodInfo.getAnnotations()) {
            if (annotation instanceof Path) {
                assertEquals(pathValue, ((Path) annotation).value());
                foundPath = true;
            }
        }
        if (!foundPath) {
            fail(method + "missing Path annotation.");
        }
    }

    protected void checkGETJSONForMethod(final Class<?> resourceClass, final String method) throws NoSuchMethodException {
        boolean foundProduces = false;
        boolean foundGet = false;
        for (Annotation annotation : resourceClass.getMethod(method).getAnnotations()) {
            if (annotation instanceof Produces) {
                assertEquals(1, ((Produces) annotation).value().length);
                assertEquals(MediaType.APPLICATION_JSON, ((Produces) annotation).value()[0]);
                foundProduces = true;
            }
            if (annotation instanceof GET) {
                foundGet = true;
            }
        }
        if (!foundGet) {
            fail(method + " missing get annotation.");
        }
        if (!foundProduces) {
            fail(method + "missing Produces annotation.");
        }
    }
}
