package com.jtbdevelopment.e_eye_o.entities.impl.reflection;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImpl;
import org.testng.annotations.Test;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/28/13
 * Time: 10:48 PM
 */
public class IdObjectInterfaceResolverImplTest {
    @SuppressWarnings("unused")
    public static interface LocalOne extends IdObject {
        public AppUserOwnedObject getAppUserOwnedObject();

        public void setAppUserOwnedObject(final AppUserOwnedObject appUserOwnedObject);

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
        public AppUserOwnedObject appUserOwnedObject;

        @Override
        public AppUserOwnedObject getAppUserOwnedObject() {
            return appUserOwnedObject;
        }

        @Override
        public void setAppUserOwnedObject(final AppUserOwnedObject appUserOwnedObject) {
            this.appUserOwnedObject = appUserOwnedObject;
        }

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
        public LocalThreeImpl() {
            super(null);
        }
    }

    private final IdObjectReflectionHelperImpl resolver = new IdObjectReflectionHelperImpl();

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

    private static class MyEntry implements Map.Entry<String, Class> {
        private String key;
        private Class value;

        public MyEntry(final String key, final Class value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public Class getValue() {
            return value;
        }

        @Override
        public Class setValue(final Class value) {
            Class oldValue = value;
            this.value = value;
            return oldValue;
        }
    }

}

