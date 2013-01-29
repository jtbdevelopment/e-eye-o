package com.jtbdevelopment.e_eye_o.entities.impl.helpers;

import com.jtbdevelopment.e_eye_o.entities.AppUserOwnedObject;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import com.jtbdevelopment.e_eye_o.entities.impl.AppUserOwnedObjectImpl;
import com.jtbdevelopment.e_eye_o.entities.impl.IdObjectImpl;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertSame;

/**
 * Date: 1/28/13
 * Time: 10:48 PM
 */
public class IdObjectInterfaceResolverImplTest {
    public static interface LocalOne extends IdObject {
    }

    public static interface LocalTwo extends LocalOne {
    }

    public static interface LocalThree extends AppUserOwnedObject {
    }

    public static class LocalOneImpl extends IdObjectImpl implements LocalOne {
    }

    public static class LocalTwoImpl extends LocalOneImpl implements LocalTwo {
    }

    public static class LocalThreeImpl extends AppUserOwnedObjectImpl implements LocalThree {
    }

    private final IdObjectInterfaceResolverImpl resolver = new IdObjectInterfaceResolverImpl();

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
}
