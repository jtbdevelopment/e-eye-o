package com.jtbdevelopment.e_eye_o.entities.wrapper;

import com.jtbdevelopment.e_eye_o.entities.AppUser;
import com.jtbdevelopment.e_eye_o.entities.IdObject;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.testng.AssertJUnit.*;

/**
 * Date: 1/5/13
 * Time: 1:34 PM
 */
public class AbstractIdObjectWrapperFactoryImplTest {
    public static interface LocalSomeIdObject extends IdObject {
        IdObject getSomeVariable();

        LocalSomeIdObject setSomeVariable(final IdObject someVariable);

        Set<IdObject> getSomeCollection();

        LocalSomeIdObject setSomeCollection(final Set<IdObject> someCollection);
    }

    public static class LocallAppUserImpl implements AppUser {
        //  not necessary to implement anything just have something
        @Override
        public String getFirstName() {
            return null;
        }

        @Override
        public AppUser setFirstName(final String firstName) {
            return null;
        }

        @Override
        public String getLastName() {
            return null;
        }

        @Override
        public AppUser setLastName(final String lastName) {
            return null;
        }

        @Override
        public String getEmailAddress() {
            return null;
        }

        @Override
        public AppUser setEmailAddress(final String emailAddress) {
            return null;
        }

        @Override
        public DateTime getLastLogin() {
            return null;
        }

        @Override
        public AppUser setLastLogin(final DateTime lastLogin) {
            return null;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public <T extends IdObject> T setId(final String id) {
            return null;
        }
    }
    public static class LocalSomeIdObjectImpl implements LocalSomeIdObject {
        private String id;
        private IdObject someVariable;
        private Set<IdObject> someCollection = new HashSet<>();

        @Override
        public String getId() {
            return id;
        }

        @Override
        public <T extends IdObject> T setId(final String id) {
            this.id = id;
            return (T) this;
        }

        @Override
        public IdObject getSomeVariable() {
            return someVariable;
        }

        @Override
        public LocalSomeIdObject setSomeVariable(final IdObject someVariable) {
            this.someVariable = someVariable;
            return this;
        }

        @Override
        public Set<IdObject> getSomeCollection() {
            return someCollection;                // not worrying about non-modifiable like for real implementations
        }

        @Override
        public LocalSomeIdObject setSomeCollection(final Set<IdObject> someCollection) {
            this.someCollection.clear();
            this.someCollection.addAll(someCollection);
            return this;
        }
    }

    public static class LocalIdObjectWrapper<T extends IdObject> implements IdObjectWrapper<T>, IdObject {
        protected T wrapped;

        public LocalIdObjectWrapper(final T entityToWrap) {
            wrapped = entityToWrap;
        }

        @Override
        public T getWrapped() {
            return wrapped;
        }

        @Override
        public String getId() {
            return wrapped.getId();
        }

        @Override
        public <T extends IdObject> T setId(final String id) {
            return wrapped.setId(id);
        }

    }

    public static class LocalSomeIdObjectWrapper extends LocalIdObjectWrapper<LocalSomeIdObject> implements LocalSomeIdObject {
        public LocalSomeIdObjectWrapper(final LocalSomeIdObject entityToWrap) {
            super(entityToWrap);
        }

        @Override
        public IdObject getSomeVariable() {
            return wrapped.getSomeVariable();
        }

        @Override
        public LocalSomeIdObject setSomeVariable(final IdObject someVariable) {
            return wrapped.setSomeVariable(wrapperFactory.wrap(someVariable));
        }

        @Override
        public Set<IdObject> getSomeCollection() {
            return wrapped.getSomeCollection();
        }

        @Override
        public LocalSomeIdObject setSomeCollection(final Set<IdObject> someCollection) {
            return wrapped.setSomeCollection(wrapperFactory.wrap(someCollection));
        }
    }

    //  broken with no constructor that accepts wrapped
    public static class LocalBrokenNoCCSomeIdObjectWrapper extends LocalSomeIdObjectWrapper implements LocalSomeIdObject {
        public  LocalBrokenNoCCSomeIdObjectWrapper() {
            super(new LocalSomeIdObjectImpl());
        }
    }

    //  broken with no public constructor that accepts wrapped
    public static class LocalBrokenNoPublicCSomeIdObjectWrapper extends LocalSomeIdObjectWrapper implements LocalSomeIdObject {
        public LocalBrokenNoPublicCSomeIdObjectWrapper(final LocalSomeIdObject wrapped) throws IllegalAccessException {
            super(wrapped);
            throw new IllegalAccessException();
        }
    }

        //  Oddly defined to allow some bad injections
    public static class SomeOtherWrapperClass implements IdObjectWrapper<IdObject>, LocalSomeIdObject {
        protected IdObject wrapped;

        public SomeOtherWrapperClass(final IdObject entityToWrap) {
            wrapped = entityToWrap;
        }

        @Override
        public IdObject getWrapped() {
            return wrapped;
        }

        @Override
        public String getId() {
            return wrapped.getId();
        }

        @Override
        public <T extends IdObject> T setId(final String id) {
            return wrapped.setId(id);
        }

        @Override
        public IdObject getSomeVariable() {
            return ((LocalSomeIdObject)wrapped).getSomeVariable();
        }

        @Override
        public LocalSomeIdObject setSomeVariable(final IdObject someVariable) {
            return ((LocalSomeIdObject)wrapped).setSomeVariable(wrapperFactory.wrap(someVariable));
        }

        @Override
        public Set<IdObject> getSomeCollection() {
            return ((LocalSomeIdObject)wrapped).getSomeCollection();
        }

        @Override
        public LocalSomeIdObject setSomeCollection(final Set<IdObject> someCollection) {
            return ((LocalSomeIdObject)wrapped).setSomeCollection(wrapperFactory.wrap(someCollection));
        }
    }

    private static class LocalIdObjectWrapperFactory extends AbstractIdObjectWrapperFactoryImpl {
        public LocalIdObjectWrapperFactory() {
            super(LocalIdObjectWrapper.class);
            addMapping(LocalSomeIdObject.class, LocalSomeIdObjectWrapper.class);
        }
    }

    private static final LocalIdObjectWrapperFactory wrapperFactory = new LocalIdObjectWrapperFactory();
    private final LocalSomeIdObjectImpl impl = new LocalSomeIdObjectImpl();
    private final LocalSomeIdObject implAsInterface = impl;
    private final LocalSomeIdObjectWrapper wrapper = new LocalSomeIdObjectWrapper(impl);
    private final LocalSomeIdObject wrapperAsInterface = wrapper;
    private final SomeOtherWrapperClass otherWrapper = new SomeOtherWrapperClass(impl);
    private final LocalSomeIdObject otherWrapperAsInterface = otherWrapper;

    @Test
    public void testNeedsWrapping() throws Exception {
        assertTrue(wrapperFactory.needsWrapping(impl));
        assertTrue(wrapperFactory.needsWrapping(implAsInterface));
        assertFalse(wrapperFactory.needsWrapping(wrapper));
        assertFalse(wrapperFactory.needsWrapping(wrapperAsInterface));
        assertTrue(wrapperFactory.needsWrapping(otherWrapper));
        assertTrue(wrapperFactory.needsWrapping(otherWrapperAsInterface));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenMappingNotOfWrapper() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(LocalSomeIdObject.class, LocalSomeIdObjectImpl.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenWrapperButNotFromBaseClass() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(LocalSomeIdObject.class, SomeOtherWrapperClass.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddMappingBlowsUpWhenClassesDontMatch() throws Exception {
        LocalIdObjectWrapperFactory testFactory = new LocalIdObjectWrapperFactory();
        testFactory.addMapping(LocalIdObjectWrapper.class, LocalSomeIdObjectWrapper.class);
    }

    @Test
    public void testWrapOnImpls() throws Exception {
        LocalSomeIdObject returned;
        returned = wrapperFactory.wrap(impl);
        assertFalse(returned == impl);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
        returned = wrapperFactory.wrap(implAsInterface);
        assertFalse(returned == impl);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
    }

    @Test
    public void testWrapOnGoodAlternateWrappers() throws Exception {
        LocalSomeIdObject returned;
        returned = wrapperFactory.wrap(otherWrapper);
        assertFalse(returned == otherWrapper);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
        returned = wrapperFactory.wrap(otherWrapperAsInterface);
        assertFalse(returned == otherWrapper);
        assertTrue(returned instanceof LocalSomeIdObjectWrapper);
    }

    @Test
    public void testWrapOnGoodCorrectlyWrapped() throws Exception {
        LocalSomeIdObject returned;
        returned = wrapperFactory.wrap(wrapper);
        assertTrue(returned == wrapper);
        returned = wrapperFactory.wrap(wrapperAsInterface);
        assertTrue(returned == wrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpOnOtherWrapperWithNullWrapped() {
        SomeOtherWrapperClass badWrapper = new SomeOtherWrapperClass(null);
        wrapperFactory.wrap(badWrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpOnOtherWrapperWithInconsistentWrapped() {
        //  forcing a bad cast
        SomeOtherWrapperClass badWrapper = new SomeOtherWrapperClass(new LocallAppUserImpl());
        wrapperFactory.wrap(badWrapper);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testWrapBlowsUpWhenUnderlyingInterfaceIsUnknown() {
        wrapperFactory.wrap(new LocallAppUserImpl());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWrapBlowsUpIfWrapperHasNoCopyConstructor() {
        LocalIdObjectWrapperFactory factory= new LocalIdObjectWrapperFactory();
        factory.addMapping(LocalSomeIdObject.class, LocalBrokenNoCCSomeIdObjectWrapper.class);
        factory.wrap(new LocalSomeIdObjectImpl());
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testWrapBlowsUpIfWrapperHasNoPublicCopyConstructor() {
        LocalIdObjectWrapperFactory factory= new LocalIdObjectWrapperFactory();
        factory.addMapping(LocalSomeIdObject.class, LocalBrokenNoPublicCSomeIdObjectWrapper.class);
        factory.wrap(new LocalSomeIdObjectImpl());
    }

    @Test
    public void testWrapNullObjectReturnsNull() {
        assertNull(wrapperFactory.wrap((IdObject) null));
        assertNull(wrapperFactory.wrap((Collection) null));
    }


    @Test
    public void testWrapCollection() throws Exception {

    }

    @Test
    public void testGetWrapperForEntity() throws Exception {
        assertEquals(LocalSomeIdObject.class, wrapperFactory.getEntityForWrapper(LocalSomeIdObjectWrapper.class));
    }

    @Test
    public void testGetEntityForWrapper() throws Exception {
        assertEquals(LocalSomeIdObjectWrapper.class, wrapperFactory.getWrapperForEntity(LocalSomeIdObject.class));
    }
}
