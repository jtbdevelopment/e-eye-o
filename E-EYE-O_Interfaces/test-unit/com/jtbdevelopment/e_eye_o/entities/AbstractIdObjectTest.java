package com.jtbdevelopment.e_eye_o.entities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Date: 12/8/12
 * Time: 6:58 PM
 */
public class AbstractIdObjectTest {
    protected static final String BLANK = "";
    protected static final String VALUE = "A Value";
    protected static final String NULL = null;

    protected void stringSetGetsWithNullsSavedAsBlanks(final Class<? extends IdObject> entityType, final String attribute) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            Object o = entityType.newInstance();
            setter.invoke(o, BLANK);
            assertEquals(BLANK, getter.invoke(o));
            o = entityType.newInstance();
            setter.invoke(o, VALUE);
            assertEquals(VALUE, getter.invoke(o));
            o = entityType.newInstance();
            setter.invoke(o, NULL);
            assertEquals(BLANK, getter.invoke(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void stringSetGetsWithBlanksAndNullsAsException(final Class<? extends IdObject> entityType, final String attribute) {
        try {
            Method setter = getSetMethod(entityType, attribute, String.class);
            Method getter = getGetMethod(entityType, attribute);
            Object o;
            o = entityType.newInstance();
            setter.invoke(o, VALUE);
            assertEquals(VALUE, getter.invoke(o));
            o = entityType.newInstance();
            boolean exception = false;
            try {
                setter.invoke(o, NULL);
            } catch (InvocationTargetException i) {
                exception = i.getTargetException() instanceof InvalidParameterException;
            }
            if (!exception) {
                fail(setter.getName() + " did not throw InvalidParameterException on null value");
            }
            o = entityType.newInstance();
            exception = false;
            try {
                setter.invoke(o, BLANK);
            } catch (InvocationTargetException i) {
                exception = i.getTargetException() instanceof InvalidParameterException;
            }
            if (!exception) {
                fail(setter.getName() + " did not throw InvalidParameterException on blank value");
            }
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected void booleanSetGetAndDefaultCheck(final Class<? extends IdObject> entityType, final String attribute, final boolean defaultValue) {
        try {
            Method setter = getSetMethod(entityType, attribute, boolean.class);
            Method getter = getIsOrGetMethod(entityType, attribute);
            Object o = entityType.newInstance();
            assertEquals(defaultValue, getter.invoke(o));
            setter.invoke(o, !defaultValue);
            assertEquals(!defaultValue, getter.invoke(o));
            setter.invoke(o, defaultValue);
            assertEquals(defaultValue, getter.invoke(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Method getIsOrGetMethod(final Class<? extends IdObject> entityType, final String attribute) throws NoSuchMethodException {
        try {
            return entityType.getMethod("is" + capitalizeAttribute(attribute));
        } catch (NoSuchMethodException e) {
            //
        }
        return getGetMethod(entityType, attribute);
    }

    protected Method getGetMethod(final Class<? extends IdObject> entityType, final String attribute) throws NoSuchMethodException {
        return entityType.getMethod("get" + capitalizeAttribute(attribute));
    }

    protected Method getSetMethod(final Class<? extends IdObject> entityType, final String attribute, final Class paramType) throws NoSuchMethodException {
        return entityType.getMethod("set" + capitalizeAttribute(attribute), paramType);
    }

    protected String capitalizeAttribute(final String attribute) {
        return attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
    }
}
