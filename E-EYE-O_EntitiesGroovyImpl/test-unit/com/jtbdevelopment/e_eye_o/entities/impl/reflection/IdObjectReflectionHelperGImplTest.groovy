package com.jtbdevelopment.e_eye_o.entities.impl.reflection

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject
import com.jtbdevelopment.e_eye_o.entities.annotations.IdObjectFieldSettings
import org.joda.time.DateTime
import org.testng.annotations.Test

import java.beans.Transient
import java.lang.reflect.Method

/**
 * Date: 12/3/13
 * Time: 9:31 PM
 */
public interface GLocalOne extends IdObject {
    @IdObjectFieldSettings(viewable = true)
    public AppUserOwnedObject getAppUserOwnedObject();

    public void setAppUserOwnedObject(final AppUserOwnedObject appUserOwnedObject);

    @IdObjectFieldSettings(viewable = false)
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

public interface GLocalTwo extends GLocalOne {
}

public interface GLocalThree extends AppUserOwnedObject {
}

class IdObjectReflectionHelperGImplTest extends GroovyTestCase {

    private static final Set<String> FIELDS = ["objectValue", "booleanValue", "intValue", "stringValues", "appUserOwnedObject", "modificationTimestamp", "id"] as Set
    public static class GLocalOneGImpl implements GLocalOne {
        String id
        DateTime modificationTimestamp
        int intValue;
        boolean booleanValue;
        Set<String> stringValues = [] as Set;
        Object objectValue;
        AppUserOwnedObject appUserOwnedObject;

        @Override
        public String getFirstStringValue() {
            if (stringValues.isEmpty())
                return null;
            else
                return stringValues.iterator().next();
        }

        @Override
        String getSummaryDescription() {
            return id
        }
    }

    public static class GLocalTwoGImpl extends GLocalOneGImpl implements GLocalTwo {
    }

    public static class GLocalThreeGImpl implements GLocalThree {
        AppUser appUser
        boolean archived
        String id
        DateTime modificationTimestamp

        @Override
        String getSummaryDescription() {
            return id
        }
    }

    private final IdObjectReflectionHelperGImpl resolver = new IdObjectReflectionHelperGImpl();

    @Test
    public void testIdInterface() throws Exception {
        assert GLocalOne.class == resolver.getIdObjectInterfaceForClass(GLocalOne.class);
    }

    @Test
    public void testIdObjectClass() throws Exception {
        assert GLocalOne.class == resolver.getIdObjectInterfaceForClass(GLocalOneGImpl.class);
    }

    @Test
    public void testIdObjectDerived() throws Exception {
        assert GLocalTwo.class == resolver.getIdObjectInterfaceForClass(GLocalTwoGImpl.class);
    }

    @Test
    public void testIdObjectDerivedFromOwned() throws Exception {
        assert GLocalThree.class == resolver.getIdObjectInterfaceForClass(GLocalThreeGImpl.class);
    }

    @Test
    public void testGetMethodsOnInterface() {
        Map<String, Method> lookup = resolver.getAllGetMethods(GLocalOne.class)
        assert FIELDS == lookup.keySet()
        assert expectedGetters() == (lookup.values() as Set)
    }

    @Test
    public void testGetMethodsOnClass() {
        Map<String, Method> lookup = resolver.getAllGetMethods(GLocalTwoGImpl.class)
        assert FIELDS == lookup.keySet()
        assert expectedGetters() == (lookup.values() as Set)
    }

    @Test
    public void testSetMethodsOnInterface() {
        Map<String, Method> lookup = resolver.getAllSetMethods(GLocalOne.class)
        assert FIELDS == lookup.keySet()
        assert expectedSetters() == (lookup.values() as Set)
    }

    @Test
    public void testSetMethodsOnClass() {
        Map<String, Method> lookup = resolver.getAllSetMethods(GLocalTwoGImpl.class)
        assert FIELDS == lookup.keySet()
        assert expectedSetters() == (lookup.values() as Set)
    }

    @Test
    public void testFieldPreferencesOnInterface() {
        def methods = expectedGetters()
        methods = methods.collect({ it.getAnnotation(IdObjectFieldSettings.class) }) as Set;
        Map<String, IdObjectFieldSettings> lookup = resolver.getAllFieldPreferences(GLocalTwo.class)
        assert FIELDS == lookup.keySet()
        assert methods == (lookup.values() as Set)
    }

    @Test
    public void testFieldPreferencesOnClass() {
        def methods = expectedGetters()
        methods = methods.collect({ it.getAnnotation(IdObjectFieldSettings.class) }) as Set;
        Map<String, IdObjectFieldSettings> lookup = resolver.getAllFieldPreferences(GLocalTwoGImpl.class)
        assert FIELDS == lookup.keySet()
        assert methods == (lookup.values() as Set)
    }

    private static Set<Method> expectedGetters() {
        def methods = GLocalOne.methods as Set
        methods.remove(GLocalOne.getMethod("getFirstStringValue"))
        methods.remove(GLocalOne.getMethod("getSummaryDescription"))
        methods = methods.findAll({ it.name.startsWith("get") || it.name.startsWith("is") }) as Set
        methods
    }

    private static Set<Method> expectedSetters() {
        def methods = GLocalOne.methods as Set
        methods = methods.findAll({ it.name.startsWith("set") }) as Set
        methods
    }
}
