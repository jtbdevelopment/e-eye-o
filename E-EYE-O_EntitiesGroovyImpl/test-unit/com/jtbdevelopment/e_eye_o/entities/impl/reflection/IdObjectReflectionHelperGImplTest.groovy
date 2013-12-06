package com.jtbdevelopment.e_eye_o.entities.impl.reflection

import com.jtbdevelopment.e_eye_o.entities.AppUser
import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject
import com.jtbdevelopment.e_eye_o.entities.IdObject
import org.joda.time.DateTime
import org.testng.annotations.Test

import java.beans.Transient

/**
 * Date: 12/3/13
 * Time: 9:31 PM
 */
public interface GLocalOne extends IdObject {
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

public interface GLocalTwo extends GLocalOne {
}

public interface GLocalThree extends AppUserOwnedObject {
}

class IdObjectReflectionHelperGImplTest extends GroovyTestCase {

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
    public void test() {
//        resolver.getAllGetMethods(GLocalOne.class)
    }
}
