package com.jtbdevelopment.e_eye_o.entities.impl.reflection;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImpl;
import org.testng.annotations.Test;

import java.beans.Transient;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/28/13
 * Time: 10:48 PM
 */
public class IdObjectInterfaceResolverImplTest {
    @SuppressWarnings("unused")
    public static interface LocalOne extends IdObject {
        public int getIntValue();

        public void setIntValue(final int value);

        public Set<String> getStringValues();

        public void setStringValues(final Set<String> stringValues);

        public boolean isBooleanValue();

        public void setBooleanValue(final boolean boolValue);

        public Object getObjectValue();

        public void setObjectValue(final Object objectValue);

        @Transient
        public String getFirstStringValue();
    }

    public static interface LocalTwo extends LocalOne {
    }

    public static interface LocalThree extends AppUserOwnedObject {
    }

    public static class LocalOneImpl extends IdObjectImpl implements LocalOne {
        public int intValue;
        public boolean boolValue;
        public Set<String> stringValues = new HashSet<>();
        public Object objectValue;

        @Override
        public int getIntValue() {
            return intValue;
        }

        @Override
        public void setIntValue(final int value) {
            this.intValue = value;
        }

        @Override
        public Set<String> getStringValues() {
            return stringValues;
        }

        @Override
        public void setStringValues(final Set<String> stringValues) {
            this.stringValues = stringValues;
        }

        @Override
        public boolean isBooleanValue() {
            return boolValue;
        }

        @Override
        public void setBooleanValue(final boolean boolValue) {
            this.boolValue = boolValue;
        }

        @Override
        public Object getObjectValue() {
            return objectValue;
        }

        @Override
        public void setObjectValue(final Object objectValue) {
            this.objectValue = objectValue;
        }

        @Override
        public String getFirstStringValue() {
            if (stringValues.isEmpty())
                return null;
            else
                return stringValues.iterator().next();
        }
    }

    public static class LocalTwoImpl extends LocalOneImpl implements LocalTwo {
    }

    public static class LocalThreeImpl extends AppUserOwnedObjectImpl implements LocalThree {
    }

    private final IdObjectInterfaceResolverImpl resolver = new IdObjectInterfaceResolverImpl();

    @Test
    public void testIdInterface() throws Exception {
        assertSame(LocalOne.class, resolver.getIdObjectInterfaceForClass(LocalOne.class));
    }

    @Test
    public void testIdObjectClass() throws Exception {
        assertSame(LocalOne.class, resolver.getIdObjectInterfaceForClass(LocalOneImpl.class));
    }

    @Test
    public void testIdObjectDerived() throws Exception {
        assertSame(LocalTwo.class, resolver.getIdObjectInterfaceForClass(LocalTwoImpl.class));
    }

    @Test
    public void testIdObjectDerivedFromOwned() throws Exception {
        assertSame(LocalThree.class, resolver.getIdObjectInterfaceForClass(LocalThreeImpl.class));
    }

    @Test
    public void testCanGetARegularGetter() throws InvocationTargetException, IllegalAccessException {
        LocalOneImpl oneImpl = new LocalOneImpl();
        oneImpl.intValue = Integer.MAX_VALUE;
        Method method = resolver.getIsOrGetMethod(oneImpl.getClass(), "intValue");
        assertEquals(oneImpl.intValue, method.invoke(oneImpl));
    }

    @Test
    public void testCanGetARegularGetterAlreadyCapitalized() throws InvocationTargetException, IllegalAccessException {
        LocalOneImpl oneImpl = new LocalOneImpl();
        oneImpl.intValue = Integer.MIN_VALUE;
        Method method = resolver.getIsOrGetMethod(oneImpl.getClass(), "IntValue");
        assertEquals(oneImpl.intValue, method.invoke(oneImpl));
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGettingNonExistentGetterThrowsException() {
        LocalOneImpl oneImpl = new LocalOneImpl();
        resolver.getIsOrGetMethod(oneImpl.getClass(), "BadGetter");
    }

    @Test
    public void testGettingIsGetterWorks() throws InvocationTargetException, IllegalAccessException {
        LocalOneImpl oneImpl = new LocalOneImpl();
        oneImpl.boolValue = !oneImpl.boolValue;
        Method method = resolver.getIsOrGetMethod(oneImpl.getClass(), "booleanValue");
        assertEquals(oneImpl.boolValue, method.invoke(oneImpl));
    }

    @Test
    public void testGettingAllGetters() {
        Set<String> expected = new HashSet<String>() {{
            add("isBooleanValue");
            add("getStringValues");
            add("getIntValue");
            add("getObjectValue");
            add("getId");
        }};
        List<Method> getters = resolver.getAllGetters(LocalOneImpl.class);
        Set<String> returned = new HashSet<>();
        for (Method getter : getters) {
            returned.add(getter.getName());
        }
        assertEquals(expected, returned);
    }

    @Test
    public void tesGettingSetterNonObject() throws InvocationTargetException, IllegalAccessException {
        boolean value = true;
        LocalOneImpl oneImpl = new LocalOneImpl();
        Method method = resolver.getSetMethod(oneImpl.getClass(), "booleanValue", boolean.class);
        method.invoke(oneImpl, value);
        assertEquals(value, oneImpl.boolValue);
    }

    @Test
    public void tesGettingSetterObject() throws InvocationTargetException, IllegalAccessException {
        Object value = Integer.parseInt("5");
        LocalOneImpl oneImpl = new LocalOneImpl();
        Method method = resolver.getSetMethod(oneImpl.getClass(), "objectValue", Object.class);
        method.invoke(oneImpl, value);
        assertSame(value, oneImpl.objectValue);
    }

    @Test
    public void tesGettingSetterCollection() throws InvocationTargetException, IllegalAccessException {
        Set<String> value = new HashSet<String>() {{
            add("test1");
            add("test2");
        }};
        LocalOneImpl oneImpl = new LocalOneImpl();
        Method method = resolver.getSetMethod(oneImpl.getClass(), "stringValues", Set.class);
        method.invoke(oneImpl, value);
        assertSame(value, oneImpl.stringValues);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void tesGettingSetterBadParamType() throws InvocationTargetException, IllegalAccessException {
        resolver.getSetMethod(LocalOneImpl.class, "booleanValue", int.class);
    }
}

